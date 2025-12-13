package com.app.hld.load_balancer;

import java.util.List;

public interface LoadBalancer {

    Server pick(List<Server> servers, Request request);

}
