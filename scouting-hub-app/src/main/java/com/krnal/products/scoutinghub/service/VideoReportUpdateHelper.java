package com.krnal.products.scoutinghub.service;

import com.krnal.products.scoutinghub.dto.VideoReportDTO;
import com.krnal.products.scoutinghub.model.VideoReport;
import org.springframework.stereotype.Component;

@Component
public class VideoReportUpdateHelper implements UpdateHelper<VideoReport, VideoReportDTO> {

    @Override
    public void set(VideoReport videoReport, VideoReportDTO videoReportDTO){
        if (videoReportDTO.getTitle() != null){
            videoReport.setTitle(videoReportDTO.getTitle());
        }
        if (videoReportDTO.getVideoLink() != null){
            videoReport.setVideoLink(videoReportDTO.getVideoLink());
        }
    }
}
