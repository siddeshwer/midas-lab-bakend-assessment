package com.midas.app.workflows;

import com.midas.app.activities.AccountActivity;
import com.midas.app.models.Account;
import com.stripe.exception.StripeException;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class CreateAccountWorkFlowImpl implements CreateAccountWorkflow {
  ActivityOptions options =
      ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(5)).build();

  private final AccountActivity activities =
      Workflow.newActivityStub(AccountActivity.class, options);

  @Override
  public Account createAccount(Account details) throws StripeException {
    return activities.createPaymentAccount(details);
  }
}
