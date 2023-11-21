package com.stg.b2b.util;


public class QueryConstants {

    private QueryConstants(){}

    /**
     * Query for fetching list of all bids
     */
    public static final String GET_BIDS_QUERY = "SELECT bid_name as bidName, " +
            "bid_active_status as bidActiveStatus, " +
            "bid_no as bidNo, " +
            "bid_over_target as bidOverTarget, " +
            "bid_po_number as bidPoNumber, " +
            "bid_external_or_internal as bidExternalOrInternal, " +
            "bid_skill_set as bidSkillSet, " +
            "bid_received_date as bidReceivedDate, " +
            "ord_broadcast_date as ordBroadcastDate, " +
            "order_no as orderNo, " +
            "bid_status as bidStatus " +
            "FROM bid_info WHERE bid_active_status = ?1 ORDER BY bid_received_date DESC";

    /**
     * Query for fetching list of all interviews
     */
    public static final String GET_INTERVIEWS_QUERY = "SELECT interview_id as interviewId, " +
            "interview_date as interviewDate, " +
            "interview_result as interviewResult " +
            "FROM interview " +
            "WHERE bid_no = ?1 ";
    /**
     * Query for fetching list of all Candidates
     */
    public static final String GET_CANDIDATES_QUERY = "SELECT candidate_file_id AS fileId, " +
            "candidate_name AS  candidateName, "+
            "order_no AS orderNo, " +
            "candidate_rate AS candidateRate, " +
            "candidate_submission_date AS candidateSubmissionDate, " +
            "candidate_declined_code AS candidateDeclinedCode, " +
            "candidate_declined_reason AS candidateDeclinedReason, " +
            "candidate_declined_date AS candidateDeclinedDate, " +
            "candidate_active_status AS candidateActiveStatus, " +
            "candidate_status AS candidateStatus "+
            "FROM candidate ";

    public static final String GET_ORDERS_QUERY = "SELECT o , GROUP_CONCAT(p.id.positionNo) as position_no, count(p.id.positionNo) as no_of_positions"
            + " FROM Order o JOIN Position p ON p.id.order.id.orderNo = o.id.orderNo AND p.id.order.id.ordBroadcastDate = o.id.ordBroadcastDate"
            + " WHERE p.posActiveStatus = :statusParam"
            + " GROUP BY o.id.orderNo,o.id.ordBroadcastDate"
            + " ORDER BY o.id.ordBroadcastDate DESC";

    public static final String GET_ALL_ORDERS_QUERY = "SELECT o.order_no AS orderNo, " +
            "o.ord_stratification AS stratification, " +
            "o.ord_broadcast_date AS broadcastDate, " +
            "o.ord_preferred_skill AS preferredSkills, " +
            "o.ord_skill_required AS skillRequired, " +
            "o.ord_job_description AS jobDescription, " +
            "o.ord_skill_group AS skillGroup, " +
            "o.ord_target_rate AS targetRate, " +
            "bu.bu_name AS buName, " +
            "bu.product_line_name AS productLineName, " +
            "m.master_name AS regionName, " +
            "ot.master_name AS orderType, " +
            "man.ll2_manager AS ll2Manager, " +
            "man.ll3_manager AS ll3Manager, " +
            "man.ll4_manager AS ll4Manager, " +
            "man.ll5_manager AS ll5Manager, " +
            "man.ll6_manager AS ll6Manager, " +
            "p.pos_active_status AS positionActiveStatus, " +
            "o.ord_active_status AS ordActiveStatus, " +
            "f.ordfol_recruiter AS recruiter, " +
            "f.ordfol_did_customer_reached_out AS didCustomerReachedOut, " +
            "f.ordfol_probability AS probability, " +
            "f.ordfol_submitted_above_target AS submittedAboveTarget, " +
            "GROUP_CONCAT(distinct p.position_no) AS positionNos, " +
            "COUNT(distinct p.position_no) AS noOfPositions " +
            "FROM order_t as o " +
            "LEFT JOIN order_follow_up as f ON f.order_no = o.order_no AND f.ord_broadcast_date = o.ord_broadcast_date " +
            "LEFT JOIN position as p ON p.order_no = o.order_no AND p.ord_broadcast_date = o.ord_broadcast_date " +
            "AND p.pos_active_status = ?1 " +
            "LEFT JOIN manager as man ON man.manager_id = o.manager_id " +
            "LEFT JOIN master as m ON m.master_id = o.mas_region_id " +
            "LEFT JOIN master as ot ON ot.master_id = o.mas_order_type_id " +
            "LEFT JOIN business_unit as bu ON bu.bu_id = o.bu_id and bu.product_line_id = o.bu_product_line_id " +
            "WHERE o.ord_active_status = ?1 " +
            "GROUP BY o.order_no, o.ord_broadcast_date," +
            "o.ord_stratification, o.ord_preferred_skill," +
            "o.ord_skill_required, o.ord_job_description," +
            "o.ord_skill_group, o.ord_target_rate," +
            "bu.bu_name, bu.product_line_name," +
            "m.master_name, ot.master_name," +
            "man.ll2_manager, man.ll3_manager," +
            "man.ll4_manager, man.ll5_manager," +
            "man.ll6_manager, p.pos_active_status," +
            "f.ordfol_recruiter, f.ordfol_did_customer_reached_out," +
            "f.ordfol_probability, f.ordfol_submitted_above_target " +
            "ORDER BY o.ord_broadcast_date DESC limit 500";

    public static final String GET_SOURCING_ORDERS_QUERY = "SELECT o.order_no AS orderNo, " +
            "o.ord_stratification AS stratification, " +
            "o.ord_broadcast_date AS broadcastDate, " +
            "o.ord_preferred_skill AS preferredSkills, " +
            "o.ord_skill_required AS skillRequired, " +
            "o.ord_job_description AS jobDescription, " +
            "o.ord_skill_group AS skillGroup, " +
            "o.ord_target_rate AS targetRate, " +
            "bu.bu_name AS buName, " +
            "bu.product_line_name AS productLineName, " +
            "m.master_name AS regionName, " +
            "ot.master_name AS orderType, " +
            "man.ll2_manager AS ll2Manager, " +
            "man.ll3_manager AS ll3Manager, " +
            "man.ll4_manager AS ll4Manager, " +
            "man.ll5_manager AS ll5Manager, " +
            "man.ll6_manager AS ll6Manager, " +
            "p.pos_active_status AS positionActiveStatus, " +
            "o.ord_active_status AS ordActiveStatus, " +
            "f.ordfol_recruiter AS recruiter, " +
            "f.ordfol_did_customer_reached_out AS didCustomerReachedOut, " +
            "f.ordfol_probability AS probability, " +
            "f.ordfol_submitted_above_target AS submittedAboveTarget, " +
            "GROUP_CONCAT(distinct p.position_no) AS positionNos, " +
            "COUNT(distinct p.position_no) AS noOfPositions " +
            "FROM order_t as o " +
            "LEFT JOIN order_follow_up as f ON f.order_no = o.order_no AND f.ord_broadcast_date = o.ord_broadcast_date " +
            "LEFT JOIN position as p ON p.order_no = o.order_no AND p.ord_broadcast_date = o.ord_broadcast_date " +
            "LEFT JOIN manager as man ON man.manager_id = o.manager_id " +
            "LEFT JOIN master as m ON m.master_id = o.mas_region_id " +
            "LEFT JOIN master as ot ON ot.master_id = o.mas_order_type_id " +
            "LEFT JOIN business_unit as bu ON bu.bu_id = o.bu_id and bu.product_line_id = o.bu_product_line_id " +
            "WHERE o.order_no in (?1) " +
            "GROUP BY o.order_no " +
            "ORDER BY o.ord_broadcast_date DESC limit 500";
    public static final String GET_ALL_ORDERS_QUERY_SOURCING ="SELECT o.order_no AS orderNo, " +
            "o.ord_stratification AS stratification, " +
            "o.ord_broadcast_date AS broadcastDate, " +
            "o.ord_preferred_skill AS preferredSkills, " +
            "o.ord_skill_required AS skillRequired, " +
            "o.ord_job_description AS jobDescription, " +
            "o.ord_skill_group AS skillGroup, " +
            "o.ord_target_rate AS targetRate, " +
            "bu.bu_name AS buName, " +
            "bu.product_line_name AS productLineName, " +
            "m.master_name AS regionName, " +
            "ot.master_name AS orderType, " +
            "man.ll2_manager AS ll2Manager, " +
            "man.ll3_manager AS ll3Manager, " +
            "man.ll4_manager AS ll4Manager, " +
            "man.ll5_manager AS ll5Manager, " +
            "man.ll6_manager AS ll6Manager, " +
            "p.pos_active_status AS positionActiveStatus, " +
            "o.ord_active_status AS ordActiveStatus, " +
            "f.ordfol_recruiter AS recruiter, " +
            "f.ordfol_did_customer_reached_out AS didCustomerReachedOut, " +
            "f.ordfol_probability AS probability, " +
            "f.ordfol_submitted_above_target AS submittedAboveTarget, " +
            "GROUP_CONCAT(distinct p.position_no) AS positionNos, " +
            "COUNT(distinct p.position_no) AS noOfPositions " +
            "FROM order_t as o " +
            "LEFT JOIN order_follow_up as f ON f.order_no = o.order_no AND f.ord_broadcast_date = o.ord_broadcast_date " +
            "LEFT JOIN position as p ON p.order_no = o.order_no AND p.ord_broadcast_date = o.ord_broadcast_date " +
            "AND p.pos_active_status = ?1 " +
            "LEFT JOIN manager as man ON man.manager_id = o.manager_id " +
            "LEFT JOIN master as m ON m.master_id = o.mas_region_id " +
            "LEFT JOIN master as ot ON ot.master_id = o.mas_order_type_id " +
            "LEFT JOIN business_unit as bu ON bu.bu_id = o.bu_id and bu.product_line_id = o.bu_product_line_id " +
            "LEFT JOIN bid_info as bi on bi.order_no = o.order_no AND bi.ord_broadcast_date = o.ord_broadcast_date "+
            "WHERE o.ord_active_status = ?1 AND (\n" +
            "    (?2 = true AND bi.bid_no IS NOT NULL and bi.bid_active_status =true ) OR\n" +
            "    (?2 = false AND bi.bid_no IS NULL)\n" +
            "  )" +
            "GROUP BY o.order_no, o.ord_broadcast_date," +
            "o.ord_stratification, o.ord_preferred_skill," +
            "o.ord_skill_required, o.ord_job_description," +
            "o.ord_skill_group, o.ord_target_rate," +
            "bu.bu_name, bu.product_line_name," +
            "m.master_name, ot.master_name," +
            "man.ll2_manager, man.ll3_manager," +
            "man.ll4_manager, man.ll5_manager," +
            "man.ll6_manager, p.pos_active_status," +
            "f.ordfol_recruiter, f.ordfol_did_customer_reached_out," +
            "f.ordfol_probability, f.ordfol_submitted_above_target " +
            "ORDER BY o.ord_broadcast_date DESC limit 500";

    public static final String GET_ORDER_QUERY = "SELECT o.order_no AS orderNo, " +
            "o.ord_stratification AS stratification, " +
            "o.ord_broadcast_date AS broadcastDate, " +
            "o.ord_preferred_skill AS preferredSkills, " +
            "o.ord_skill_required AS skillRequired, " +
            "o.ord_job_description AS jobDescription, " +
            "o.ord_skill_group AS skillGroup, " +
            "o.ord_target_rate AS targetRate, " +
            "bu.bu_name AS buName, " +
            "bu.product_line_name AS productLineName, " +
            "m.master_name AS regionName, " +
            "ot.master_name AS orderType, " +
            "man.ll2_manager AS ll2Manager, " +
            "man.ll3_manager AS ll3Manager, " +
            "man.ll4_manager AS ll4Manager, " +
            "man.ll5_manager AS ll5Manager, " +
            "man.ll6_manager AS ll6Manager, " +
            "p.pos_active_status AS positionActiveStatus, " +
            "o.ord_active_status AS ordActiveStatus, " +
            "f.ordfol_recruiter AS recruiter, " +
            "f.ordfol_did_customer_reached_out AS didCustomerReachedOut, " +
            "f.ordfol_probability AS probability, " +
            "f.ordfol_submitted_above_target AS submittedAboveTarget, " +
            "GROUP_CONCAT(distinct p.position_no) AS positionNos, " +
            "COUNT(distinct p.position_no) AS noOfPositions " +
            "FROM order_t as o " +
            "LEFT JOIN order_follow_up as f ON f.order_no = o.order_no AND f.ord_broadcast_date = o.ord_broadcast_date " +
            "LEFT JOIN position as p ON p.order_no = o.order_no AND p.ord_broadcast_date = o.ord_broadcast_date " +
            "LEFT JOIN manager as man ON man.manager_id = o.manager_id " +
            "LEFT JOIN master as m ON m.master_id = o.mas_region_id " +
            "LEFT JOIN master as ot ON ot.master_id = o.mas_order_type_id " +
            "LEFT JOIN business_unit as bu ON bu.bu_id = o.bu_id and bu.product_line_id = o.bu_product_line_id " +
            "WHERE o.order_no= ?1 and o.ord_broadcast_date= ?2";


    public static final String GET_REGION_ORDER_TYPES ="SELECT m2.master_name  FROM order_t as o " +
            "JOIN master as m ON o.mas_region_id=m.master_id " +
            "JOIN master as m2 ON o.mas_order_type_id=m2.master_id " +
            "WHERE o.ord_active_status = true " +
            "group by m2.master_name";

    public static final String GET_BU_ORDER_TYPES ="SELECT m2.master_name FROM order_t as o " +
            "JOIN business_unit as bu ON o.bu_id=bu.bu_id " +
            "JOIN master as m2 ON o.mas_order_type_id=m2.master_id " +
            "WHERE o.ord_active_status = true " +
            "group by m2.master_name";
}
