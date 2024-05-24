package com.example.final_project.util;

import com.example.final_project.model.dto.request.event.SearchEventRequest;
import org.springframework.stereotype.Component;


@Component
public class SqlScriptFilterEvent {
    // count record when search event
    public static String getSqlScriptCountEventRecord(SearchEventRequest searchEventRequest, Integer orgId){
        String sql = "SELECT COUNT(*) FROM event WHERE org_id = " + orgId;

        if(searchEventRequest.getEventName() != null && !searchEventRequest.getEventName().isEmpty())
            sql += " AND event_name ILIKE '%" + searchEventRequest.getEventName() + "%'";
        if(searchEventRequest.getCategoryId() != null)
            sql += " AND cate_id = " + searchEventRequest.getCategoryId();
        if(searchEventRequest.getStatus() != null)
            sql += " AND is_open = " + searchEventRequest.getStatus();

        if(searchEventRequest.getStartDateTime() != null && searchEventRequest.getEndDateTime() != null) {
            sql += " AND (('" + searchEventRequest.getStartDateTime() + "' >= start_date AND '" +
                    searchEventRequest.getEndDateTime() + "' <= end_date) OR ('" + searchEventRequest.getStartDateTime() + "' <= start_date AND '" +

                    searchEventRequest.getEndDateTime() + "' >= end_date))"
            ;
        }
        else if(searchEventRequest.getStartDateTime() != null)
            sql += " AND DATE('" + searchEventRequest.getStartDateTime() + "') = DATE(start_date)";
        else if(searchEventRequest.getEndDateTime() != null)
            sql += " AND DATE('" + searchEventRequest.getEndDateTime() + "') = DATE(end_date)";

        return sql;
    }

    public static String getSqlScriptSearchEvent(SearchEventRequest searchEventRequest, Integer orgId, Integer offset, Integer limit){
        String sql = "SELECT * FROM event WHERE org_id = " + orgId;

        if(searchEventRequest.getEventName() != null && !searchEventRequest.getEventName().isEmpty())
            sql += " AND event_name ILIKE '%" + searchEventRequest.getEventName() + "%' ";

        if(searchEventRequest.getCategoryId() != null)
            sql += " AND cate_id = " + searchEventRequest.getCategoryId();
        if(searchEventRequest.getStatus() != null)
            sql += " AND is_open = " + searchEventRequest.getStatus();

        if(searchEventRequest.getStartDateTime() != null && searchEventRequest.getEndDateTime() != null) {
            sql += " AND (" +
                        "('" + searchEventRequest.getStartDateTime() + "' >= start_date AND '" +
                        searchEventRequest.getEndDateTime() + "' <= end_date) OR ('" + searchEventRequest.getStartDateTime() + "' <= start_date AND '" +

                        searchEventRequest.getEndDateTime() + "' >= end_date)" +
                    ")"
            ;
        }
        else if(searchEventRequest.getStartDateTime() != null)
            sql += " AND DATE('" + searchEventRequest.getStartDateTime() + "') = DATE(start_date)";
        else if(searchEventRequest.getEndDateTime() != null)
            sql += " AND DATE('" + searchEventRequest.getEndDateTime() + "') = DATE(end_date)";
        sql += "ORDER BY event_name LIMIT " + limit + " OFFSET " + offset;
        return sql;
    }
}
