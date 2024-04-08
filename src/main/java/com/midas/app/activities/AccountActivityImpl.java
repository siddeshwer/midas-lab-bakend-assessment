package com.midas.app.activities;

import com.midas.app.enums.ProviderType;
import com.midas.app.models.Account;
import com.midas.app.providers.external.stripe.StripeConfiguration;
import com.midas.app.providers.external.stripe.StripePaymentProvider;
import com.midas.app.providers.payment.CreateAccount;
import com.midas.app.repositories.AccountRepository;
import com.stripe.exception.StripeException;

public class AccountActivityImpl implements AccountActivity {
  AccountRepository accountRepository;

  @Override
  public Account saveAccount(Account account) {
    Account localAccount = new Account();
    localAccount.setFirstName(account.getFirstName());
    localAccount.setLastName(account.getLastName());
    localAccount.setEmail(account.getEmail());
    localAccount.setProviderId(account.getProviderId());
    localAccount.setProviderType(ProviderType.STRIPE.toString());
    return accountRepository.save(localAccount);
  }

  @Override
  public Account createPaymentAccount(Account account) throws StripeException {
    CreateAccount createAccountDetails =
        new CreateAccount(
            account.getProviderId(),
            account.getFirstName(),
            account.getLastName(),
            account.getEmail());
    StripePaymentProvider stripePaymentProvider =
        new StripePaymentProvider(new StripeConfiguration());
    Account stripeCustomerAccount = stripePaymentProvider.createAccount(createAccountDetails);

    saveAccount(stripeCustomerAccount);
    return stripeCustomerAccount;
  }
}
