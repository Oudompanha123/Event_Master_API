package com.example.final_project.repository;

import com.example.final_project.model.CategoryEventCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DashboardRepository {

    @Select("""
        SELECT COUNT(*) FROM event WHERE org_id = #{orgId};
    """)
    Integer getEventCount(Integer orgId);

    @Select("""
        SELECT COUNT(*) FROM event WHERE org_id = #{orgId} AND is_post = true;
    """)
    Integer getPublishEventCount(Integer orgId);

    @Select("""
        SELECT COUNT(*) FROM attendee INNER JOIN event ON event.event_id = attendee.event_id                                                                   
        WHERE event.org_id = #{orgId};
    """)
    Integer getAttendeeCount(Integer orgId);

    @Select("""
        SELECT COUNT(*) FROM member WHERE org_id = #{orgId} AND is_approve = true;
    """)
    Integer getMemberCount(Integer orgId);

    @Select("""
        SELECT cate_name, COUNT(*) FROM category INNER JOIN event ON category.cate_id = event.cate_id
        WHERE category.org_id = #{orgId} AND event.org_id = #{orgId}  GROUP BY category.cate_id;
    """)
    @Result(property = "cateName", column = "cate_name")
    List<CategoryEventCount> getCategoryEventCountList(Integer orgId);
}
