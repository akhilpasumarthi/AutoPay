package com.example.autopay;

public class trasaction {
    private String from;
    private long timestamp;
    private String status;
    //private String type;

    private void transaction(){}
    private void transaction1(String from,String to){
        this.from=from;
        this.timestamp=timestamp;
        this.status=status;
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

   // public String getType() {
     //   return type;
    //}

    //public void setType(String type) {
      //  this.type = type;
    //}
}
