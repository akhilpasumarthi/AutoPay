package com.example.autopay;

public class trasaction {
    private String from;
    private long timestamp;
    private String status;
    private long amount;
    private String address;
    private String to;
    //private String type;

    private void transaction(){}
    private void transaction1(String from,long amount){
        this.from=from;
        this.timestamp=timestamp;
        this.status=status;
        this.amount=amount;
        this.address=address;
        this.to=to;
        //this.type=type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    // public String getType() {
     //   return type;
    //}

    //public void setType(String type) {
      //  this.type = type;
    //}
}
