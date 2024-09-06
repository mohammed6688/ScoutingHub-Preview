package com.krnal.products.scoutinghub.service;

import com.krnal.products.scoutinghub.dao.VideoReportRepo;
import com.krnal.products.scoutinghub.dto.VideoReportDTO;
import com.krnal.products.scoutinghub.mapper.VideoReportMapper;
import com.krnal.products.scoutinghub.model.VideoReport;
import com.krnal.products.scoutinghub.specification.VideoReportSpecification;
import com.krnal.products.scoutinghub.types.SearchCriteria;
import com.krnal.products.scoutinghub.types.VideoReportResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.krnal.products.scoutinghub.constants.Constant.*;
import static com.krnal.products.scoutinghub.utils.Utilities.createLogMessage;

@Service
public class VideoReportService {
    Logger logger = LoggerFactory.getLogger(VideoReportService.class);

    @Autowired
    VideoReportRepo videoReportRepo;
    @Autowired
    VideoReportMapper videoReportMapper;
    @Autowired
    VideoReportUpdateHelper videoReportUpdateHelper;

    public VideoReportResponse getVideoReports() {
        String c = "VideoReportService";
        String m = "getVideoReports";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            List<VideoReport> videoReports = videoReportRepo.findAll();
            List<VideoReportDTO> videoReportDTOList = videoReports.stream()
                    .map(videoReport -> videoReportMapper.getVideoReportDto(videoReport))
                    .toList();
            logger.info(createLogMessage(c, m, "Success"));
            return new VideoReportResponse(videoReportDTOList, videoReports.size());
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public VideoReportResponse getVideoReports(Pageable pageable) {
        String c = "VideoReportService";
        String m = "getVideoReports";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Page<VideoReport> videoReports = videoReportRepo.findAll(pageable);
            List<VideoReportDTO> videoReportDTOList = videoReports.stream()
                    .map(videoReport -> videoReportMapper.getVideoReportDto(videoReport))
                    .toList();
            logger.info(createLogMessage(c, m, "Success"));
            return new VideoReportResponse(videoReportDTOList, videoReports.getTotalElements());
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public VideoReportDTO getVideoReport(Integer id) {
        String c = "VideoReportService";
        String m = "getVideoReports";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Optional<VideoReport> videoReport = videoReportRepo.findById(id);
            if (videoReport.isPresent()) {
                VideoReportDTO videoReportDTO = videoReportMapper.getVideoReportDto(videoReport.get());
                logger.info(createLogMessage(c, m, "Success"));
                return videoReportDTO;
            }
            throw new RuntimeException(VIDEO_REPORT_ID_NOT_EXIST);
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public VideoReportDTO addVideoReport(VideoReportDTO videoReportDTO) {
        String c = "VideoReportService";
        String m = "addVideoReport";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Optional<VideoReport> videoReportOptional = videoReportRepo.findByTitle(videoReportDTO.getTitle());
            if (videoReportOptional.isEmpty()) { // if provided zone name is not exist
                VideoReport videoReport = videoReportRepo.save(videoReportMapper.getVideoReport(videoReportDTO));
                logger.info(createLogMessage(c, m, "Success"));
                return videoReportMapper.getVideoReportDto(videoReport);
            }
            throw new RuntimeException(VIDEO_REPORT_NAME_EXIST);
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public VideoReportDTO updateVideoReport(VideoReportDTO videoReportDTO) {
        String c = "VideoReportService";
        String m = "updateVideoReport";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Optional<VideoReport> videoReportOptional = videoReportRepo.findById(videoReportDTO.getId());
            if (videoReportOptional.isPresent()) { // if provided zone id is existed
                VideoReport videoReport = videoReportOptional.get();
                videoReportUpdateHelper.set(videoReport, videoReportDTO);
                videoReport = videoReportRepo.save(videoReport);
                logger.info(createLogMessage(c, m, "Success"));
                return videoReportMapper.getVideoReportDto(videoReport);
            }
            throw new RuntimeException(ZONE_ID_EXIST);
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public void deleteVideoReport(Integer id) {
        String c = "VideoReportService";
        String m = "deleteVideoReport";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Optional<VideoReport> videoReportOptional = videoReportRepo.findById(id);
            if (videoReportOptional.isPresent()) { // if provided zone id is existed
                videoReportRepo.deleteById(id);
                logger.info(createLogMessage(c, m, "Success"));
                return;
            }
            throw new RuntimeException(VIDEO_REPORT_ID_NOT_EXIST);
        } catch (DataIntegrityViolationException e){
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(VIDEO_REPORT_IS_REFERENCED, e); // Re-throw as unchecked exception
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }


    public VideoReportResponse searchVideoReport(List<SearchCriteria> params, Pageable pageable) {
        String c = "VideoReportService";
        String m = "searchVideoReport";

        try {
            logger.info(createLogMessage(c, m, "Start"));

            if (params == null || params.isEmpty()) {
                return getVideoReports(pageable);
            }
            Specification<VideoReport> spec = Specification.where(createSpecification(params.getFirst()));

            for (int i = 1; i < params.size(); i++) {
                spec = spec.and(createSpecification(params.get(i)));
            }

            Page<VideoReport> videoReports = videoReportRepo.findAll(spec, pageable);
                    List<VideoReportDTO> videoReportDTOList = videoReports.stream()
                    .map(videoReport -> videoReportMapper.getVideoReportDto(videoReport))
                    .toList();

            return new VideoReportResponse(videoReportDTOList, videoReports.getTotalElements());

        } catch (Exception e){
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    private Specification<VideoReport> createSpecification(SearchCriteria criteria) {
        return new VideoReportSpecification(criteria);
    }
}
