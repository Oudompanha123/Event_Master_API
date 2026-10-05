package com.example.final_project.repository;

import com.example.final_project.PostgresTestBase;
import com.example.final_project.model.Asset;
import com.example.final_project.model.dto.request.AssetRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Smoke test for the hand-written SQL in the mappers. It is not about asset
 * behaviour as such - it is there so a typo in a query fails the build instead
 * of reaching the deployed container.
 */
@Tag("db")
@Transactional
class AssetRepositoryTest extends PostgresTestBase {

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Integer orgId;

    @BeforeEach
    void createOrganization() {
        orgId = jdbcTemplate.queryForObject(
                "INSERT INTO organization (org_name, code) VALUES ('Test Org', 'TST01') RETURNING org_id",
                Integer.class);
    }

    @Test
    void insertsAnAssetAndReadsItBack() {
        Asset inserted = assetRepository.insertAsset(new AssetRequest("Plastic Chair", 25f, "pcs"), orgId);

        assertThat(inserted.getAssetId()).isPositive();
        assertThat(inserted.getAssetName()).isEqualTo("Plastic Chair");
        assertThat(assetRepository.findAssetById(inserted.getAssetId(), orgId)).isNotNull();
    }

    @Test
    void searchByNameIsCaseInsensitive() {
        assetRepository.insertAsset(new AssetRequest("Plastic Chair", 25f, "pcs"), orgId);
        assetRepository.insertAsset(new AssetRequest("Round Table", 10f, "pcs"), orgId);

        // ILIKE + CONCAT is PostgreSQL-specific and easy to break when edited.
        List<Asset> found = assetRepository.getAllAssetsByName("plastic", 0, 10, orgId);

        assertThat(found).extracting(Asset::getAssetName).containsExactly("Plastic Chair");
        assertThat(assetRepository.getTotalAssetRecordsFromSearch("plastic", orgId)).isEqualTo(1);
        assertThat(assetRepository.getTotalAssetRecords(orgId)).isEqualTo(2);
    }

    @Test
    void doesNotLeakAssetsAcrossOrganizations() {
        Integer otherOrgId = jdbcTemplate.queryForObject(
                "INSERT INTO organization (org_name, code) VALUES ('Other Org', 'TST02') RETURNING org_id",
                Integer.class);
        assetRepository.insertAsset(new AssetRequest("Plastic Chair", 25f, "pcs"), orgId);

        assertThat(assetRepository.findAllAssets(0, 10, otherOrgId)).isEmpty();
    }
}
