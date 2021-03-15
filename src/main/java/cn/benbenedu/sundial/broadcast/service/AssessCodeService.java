package cn.benbenedu.sundial.broadcast.service;

import cn.benbenedu.sundial.broadcast.model.AssessTokenTargetType;
import cn.benbenedu.sundial.broadcast.repository.examstation.AuxiliaryTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AssessCodeService {

    private final AuxiliaryTokenRepository auxiliaryTokenRepository;

    public AssessCodeService(
            final AuxiliaryTokenRepository auxiliaryTokenRepository) {

        this.auxiliaryTokenRepository = auxiliaryTokenRepository;
    }

    public Boolean verifyAssessCode(
            final AssessTokenTargetType targetType,
            final String targetId,
            final String assessCode) {

        return auxiliaryTokenRepository.existsByCodeAndTargetTypeAndTargetId(
                assessCode, targetType.toString(), targetId);
    }
}
