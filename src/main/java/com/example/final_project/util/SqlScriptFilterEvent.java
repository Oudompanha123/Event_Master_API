package com.example.final_project.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class SqlScriptFilterEvent {
    // count record when search event
    public static String getSqlScriptCountEventRecord(String eventName, Integer categoryId, Boolean status, LocalDateTime startDateTime, LocalDateTime endDateTime, Integer orgId){
        String sql = "SELECT COUNT(*) FROM event WHERE org_id = " + orgId;

        if(eventName != null && !eventName.isEmpty())
            sql += " AND event_name ILIKE '%" + eventName + "%'";
        if(categoryId != null)
            sql += " AND cate_id = " + categoryId;
        if(status != null)
            sql += " AND is_open = " + status;

        if(startDateTime != null && endDateTime != null) {
            sql += " AND (('" + startDateTime + "' >= start_date AND '" +
                    endDateTime + "' <= end_date) OR ('" + startDateTime + "' <= start_date AND '" +

                    endDateTime + "' >= end_date))"
            ;
        }
        else if(startDateTime != null)
            sql += " AND DATE('" + startDateTime + "') = DATE(start_date)";
        else if(endDateTime != null)
            sql += " AND DATE('" + endDateTime + "') = DATE(end_date)";

        return sql;
    }

    public static String getSqlScriptSearchEvent(String eventName, Integer categoryId, Boolean status, LocalDateTime startDateTime, LocalDateTime endDateTime, Integer orgId, Integer offset, Integer limit){
        String sql = "SELECT * FROM event WHERE org_id = " + orgId;

        if(eventName != null && !eventName.isEmpty())
            sql += " AND event_name ILIKE '%" + eventName + "%' ";

        if(categoryId != null)
            sql += " AND cate_id = " + categoryId;
        if(status != null)
            sql += " AND is_open = " + status;

        if(startDateTime != null && endDateTime != null) {
            sql += " AND (" +
                        "('" + startDateTime + "' >= start_date AND '" +
                        endDateTime + "' <= end_date) OR ('" + startDateTime + "' <= start_date AND '" +

                        endDateTime + "' >= end_date)" +
                    ")"
            ;
        }
        else if(startDateTime != null)
            sql += " AND DATE('" + startDateTime + "') = DATE(start_date)";
        else if(endDateTime != null)
            sql += " AND DATE('" + endDateTime + "') = DATE(end_date)";
        sql += " ORDER BY start_date LIMIT " + limit + " OFFSET " + offset;
        return sql;
    }

    public static String getSqlScriptSearchEventOnLandingPage(String eventName, String categoryName, Boolean status, LocalDateTime startDateTime, LocalDateTime endDateTime){

        if (eventName == null && categoryName == null && status == null && startDateTime == null && endDateTime == null) {
            return "SELECT * FROM event WHERE 1=0"; // No rows will be returned
        }

        String sql = """
                 SELECT c.cate_name, e.event_id, e.event_name, e.description, e.start_date,
                 e.address, e.poster, e.is_open, o.org_name, o.logo FROM event e INNER JOIN category c ON c.cate_id = e.cate_id
                 INNER JOIN organization o ON o.org_id = e.org_id
                 WHERE e.is_post = true
                """;

        if(eventName != null && !eventName.isEmpty())
            sql += " AND event_name ILIKE '%" + eventName + "%' ";

        if(categoryName != null)
            sql += " AND c.cate_name = '" + categoryName + "' ";
        if(status != null)
            sql += " AND is_open = " + status;

        if(startDateTime != null && endDateTime != null) {
            sql += " AND (" +
                    "('" + startDateTime + "' >= start_date AND '" +
                    endDateTime + "' <= end_date) OR ('" + startDateTime + "' <= start_date AND '" +

                    endDateTime + "' >= end_date)" +
                    ")"
            ;
        }
        else if(startDateTime != null)
            sql += " AND DATE('" + startDateTime + "') = DATE(start_date)";
        else if(endDateTime != null)
            sql += " AND DATE('" + endDateTime + "') = DATE(end_date)";

        sql += " ORDER BY e.start_date";
        return sql;
    }

}
