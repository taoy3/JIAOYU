package com.taoy3.freight.bean;

import java.io.Serializable;

/**
 * Created by taoy2 on 15-11-20.
 */
public class OrderEntity extends SearchBean implements Serializable{
    private String start_port;
    private String dest_port;
    private String ticket_number;
    private String operator;

    public String getStart_port() {
        return start_port;
    }

    public void setStart_port(String start_port) {
        this.start_port = start_port;
    }

    public String getDest_port() {
        return dest_port;
    }

    public void setDest_port(String dest_port) {
        this.dest_port = dest_port;
    }

    public String getTicket_number() {
        return ticket_number;
    }

    public void setTicket_number(String ticket_number) {
        this.ticket_number = ticket_number;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public OrderEntity() {
    }
    @Override
    public String getId() {
        return ticket_number;
    }
    public OrderEntity(String start_port, String dest_port, String ticket_number, String operator) {
        this.start_port = start_port;
        this.dest_port = dest_port;
        this.ticket_number = ticket_number;
        this.operator = operator;
    }
}
