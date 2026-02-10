SELECT
    rl.id_rail_line,
    fs.name AS start_facility,
    fe.name AS end_facility,
    rls.id_rail_line_segment      AS segment_id,
    rls.Is_Electrified_Line				AS Electrified,
    rls.Length					AS Length_M,
    rls.speed_limit,
    rls.number_tracks,
    tg.Gauge_Size                        AS track_gauge
FROM rail_line rl
         JOIN facility fs
              ON fs.id_facility = rl.start_facilityid_facility
         JOIN facility fe
              ON fe.id_facility = rl.end_facilityid_facility
         JOIN rail_line_rail_line_segment assoc
              ON assoc.rail_lineid_rail_line = rl.id_rail_line
         JOIN rail_line_segment rls
              ON rls.id_rail_line_segment = assoc.rail_line_segmentid_rail_line_segment
         JOIN track_gauge tg
              ON tg.id_track_gauge = rls.track_gaugeid_track_gauge
WHERE rl.id_rail_line IN (
    SELECT a.rail_lineid_rail_line
    FROM rail_line_rail_line_segment a
    GROUP BY a.rail_lineid_rail_line
    HAVING COUNT(*) = 1
)
ORDER BY rl.id_rail_line;
