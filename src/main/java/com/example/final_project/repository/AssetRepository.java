package com.example.final_project.repository;

import com.example.final_project.model.Asset;
import com.example.final_project.model.dto.request.AssetRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AssetRepository {
    @Select("""
        SELECT * FROM asset WHERE org_id = #{orgId} ORDER BY update_at DESC LIMIT #{limit} OFFSET #{offset};
    """)
    @Results(id = "AssetMapper", value = {
            @Result(property = "assetId", column = "asset_id"),
            @Result(property = "assetName", column = "asset_name"),
            @Result(property = "createdAt", column = "created_at")
    })
    List<Asset> findAllAssets(Integer offset, Integer limit, Integer orgId);

    @Select("""
        SELECT * FROM asset where asset_name ILIKE CONCAT('%', #{assetName}, '%') AND org_id = #{orgId} ORDER BY update_at DESC LIMIT #{limit} OFFSET #{offset};
    """)
    @ResultMap("AssetMapper")
    List<Asset> getAllAssetsByName(String assetName, Integer offset, Integer limit, Integer orgId);

    //find asset by id
    @Select("""
        SELECT * FROM asset where asset_id = #{id} AND org_id = #{orgId};
    """)
    @ResultMap("AssetMapper")
    Asset findAssetById(Integer id, Integer orgId);


    @Select("""
        INSERT INTO asset (asset_name, qty, unit,org_id)
        VALUES (#{asset.assetName}, #{asset.qty}, #{asset.unit}, #{orgId}) RETURNING *
    """)
    @ResultMap("AssetMapper")
    Asset insertAsset(@Param("asset") AssetRequest assetRequest, Integer orgId);

    @Select("""
        UPDATE asset SET asset_name = #{asset.assetName}, unit = #{asset.unit}, qty = #{asset.qty}, update_at = current_timestamp WHERE asset_id = #{id} AND org_id = #{orgId} RETURNING *
    """)
    @ResultMap("AssetMapper")
    Asset updateAsset(Integer id, @Param("asset") AssetRequest assetRequest, Integer orgId);

    @Delete("""
        DELETE FROM asset where asset_id = #{id} AND org_id = #{orgId};
    """)
    void deleteAssetById(Integer id, Integer orgId);

    @Select("""
        SELECT COUNT(*) FROM asset WHERE org_id = #{orgId};
    """)
    Integer getTotalAssetRecords(Integer orgId);

    @Select("""
        SELECT COUNT(*) FROM asset where asset_name ILIKE CONCAT('%', #{assetName}, '%') AND org_id = #{orgId};
    """)
    Integer getTotalAssetRecordsFromSearch(String assetName, Integer orgId);

    @Select("""
        SELECT asset_name FROM asset WHERE org_id = #{orgId};
    """)
    List<String> getAllAssetNames(Integer orgId);

    @Update("""
        UPDATE asset SET qty = #{assetQty} WHERE asset_id = #{assetId} AND org_id = #{orgId};
    """)
    void updateAssetQty(Integer assetId, Integer orgId, float assetQty);
}

