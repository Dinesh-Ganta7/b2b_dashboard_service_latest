package com.stg.b2b.upload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UniqueOrderDto  {
    private Integer orderNo;

    private Date ordBroadcastDate;
    private String stratification;
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UniqueOrderDto other = (UniqueOrderDto) obj;
        if(this.orderNo!=null && this.ordBroadcastDate!=null){
            return (this.orderNo.equals(other.getOrderNo()) && this.ordBroadcastDate.equals(other.getOrdBroadcastDate()));
        }else{
            return false;
        }
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.orderNo, this.ordBroadcastDate);
    }
}
