package com.connexta.interview.graphql.schema;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import graphql.servlet.SimpleGraphQLServlet;

public class RunServlet {

    /**
     * Runs servlet so we use GraphiQL to see schema.
     * Use chrome extension with http://localhost:8994/schema.json endpoint.
     */
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
