package com.aixohub.portfolio.snapshot.filter;

import com.aixohub.portfolio.model.Account;
import com.aixohub.portfolio.model.Client;
import com.aixohub.portfolio.model.Portfolio;

public class EmptyFilter implements ClientFilter {

    @Override
    public Client filter(Client client) {
        ReadOnlyClient pseudoClient = new ReadOnlyClient(client);

        Account account = new Account();
        Portfolio portfolio = new Portfolio();
        portfolio.setReferenceAccount(account);

        ReadOnlyAccount pa = new ReadOnlyAccount(account);
        pseudoClient.internalAddAccount(pa);

        ReadOnlyPortfolio pp = new ReadOnlyPortfolio(portfolio);
        pp.setReferenceAccount(pa);
        pseudoClient.internalAddPortfolio(pp);

        return pseudoClient;
    }

}
