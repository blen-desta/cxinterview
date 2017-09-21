package com.connexta.interview.graphql.schema;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import graphql.servlet.SimpleGraphQLServlet;

public class RunServlet {

    public static void main(String[] args) throws Exception {
        Server server = new Server(8994);
        ServletContextHandler context = new ServletContextHandler(server, "/");
        context.addServlet(TestServlet(), "/*");
        server.start();
    }

    private static ServletHolder TestServlet() {
        return new ServletHolder(new SimpleGraphQLServlet(GraphQLProviderKt.createSchema()));
    }

}
