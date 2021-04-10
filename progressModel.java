package com.daat.productivity;

public class progressModel {
    String start,end,mid;

    public progressModel(String start, String end, String mid) {
        this.start = start;
        this.end = end;
        this.mid = mid;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }
}
