package com.midas.app.providers.external.stripe;

import com.midas.app.enums.ProviderType;
import com.midas.app.models.Account;
import com.midas.app.providers.payment.CreateAccount;
import com.midas.app.providers.payment.PaymentProvider;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
public class StripePaymentProvider implements PaymentProvider {
  private final Logger logger = LoggerFactory.getLogger(StripePaymentProvider.class);

  private final StripeConfiguration configuration;

  /** providerName is the name of the payment provider */
  @Override
  public String providerName() {
    return "stripe";
  }

  /**
   * createAccount creates a new account in the payment provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  @Value("${stripe.api-key}")
  String stripeApiKey;

  @Override
  public Account createAccount(CreateAccount details) throws StripeException {
    Stripe.apiKey =
        "sk_test_51P3IfiSJuvMIU4Mu9gTXE96HsgETEdTioc6z7bANcJstTBxIkxPhiywKbqxpDQPJWGyohWzJFaC96RdqzhkeEopB00Uir8kz40";
    CustomerCreateParams params =
        CustomerCreateParams.builder()
            .setName(details.getFirstName().concat(details.getLastName()))
            .setEmail(details.getEmail())
            .build();

    Customer customer = Customer.create(params);
    Account account = new Account();
    account.setProviderId(customer.getId());
    account.setFirstName(customer.getName());
    account.setLastName(customer.getName());
    account.setProviderType(ProviderType.STRIPE.toString());
    account.setEmail(customer.getEmail());

    return account;
  }
}
