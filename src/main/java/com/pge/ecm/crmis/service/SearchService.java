package com.pge.ecm.crmis.service;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.impl.util.StringUtil;
import com.pge.ecm.crmis.dfc.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchService {

    public String repository;
    public String repository_user;
    public String repository_password;

    public SearchService(String repository, String user, String password) {
        this.repository = repository;
        this.repository_user = user;
        this.repository_password = password;
    }

    public List<Map<String, String>> execute(String objectType, String sdcClause,
                                             String whereClause, String select_list) throws Exception {

        IDfCollection collection = null;
        SessionManager sessionManager = null;
        IDfSession session = null;
        String[] select_items = select_list.split(",");
        List<Map<String, String>> returnList = null;
        StringBuilder query = new StringBuilder("select ")
                .append(select_list).append(" from ").append(objectType);

        if (!StringUtil.isEmptyOrNull(sdcClause)) {
            query.append(" search document contains '").append(sdcClause).append("'");
        }

        if (!StringUtil.isEmptyOrNull(whereClause)) {
            query.append(" where ").append(whereClause);
        }

        try {
            System.out.println("Connecting to " + repository);
            System.out.println("Creating session");

            sessionManager = new SessionManager();
            session = sessionManager.getSession(repository, repository_user, repository_password);
            System.out.println("Session created " + " for user " + repository_user);
            System.out.println("Executing query " + query.toString());
            DfQuery queryObject = new DfQuery();
            queryObject.setDQL(query.toString());
            collection = queryObject.execute(session, IDfQuery.DF_EXECREAD_QUERY);

            if (collection != null) {
                returnList = new ArrayList<>();
                while (collection.next()) {
                    Map<String, String> row = new HashMap<>();
                    for(String item : select_items)
                        row.put(item, collection.getString(item));
                    returnList.add(row);
                    System.out.println(row.toString());
                }
            }
        } finally {
            if (collection != null) {
                collection.close();
            }
        }

        return returnList;

    }
}
