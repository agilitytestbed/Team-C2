package nl.utwente.ing.balance;

import java.io.Serializable;
import java.util.Date;


public class Candlestick implements Serializable {

    private Float open;

    private Float close;

    private Float high;

    private Float low;

    private Float volume;

    private Date timestamp;

    public Candlestick() {}

    public Candlestick(Float open, Float close, Float high, Float low, Float volume, Date timestamp) {
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        this.volume = volume;
        this.timestamp = timestamp;
    }

    public Float getOpen() {
        return open;
    }

    public void setOpen(Float open) {
        this.open = open;
    }

    public Float getClose() {
        return close;
    }

    public void setClose(Float close) {
        this.close = close;
    }

    public Float getHigh() {
        return high;
    }

    public void setHigh(Float high) {
        this.high = high;
    }

    public Float getLow() {
        return low;
    }

    public void setLow(Float low) {
        this.low = low;
    }

    public Float getVolume() {
        return volume;
    }

    public void setVolume(Float volume) {
        this.volume = volume;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
