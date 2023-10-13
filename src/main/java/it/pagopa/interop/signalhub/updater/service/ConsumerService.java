package it.pagopa.interop.signalhub.updater.service;

import it.pagopa.interop.signalhub.updater.model.AgreementEventDto;
import it.pagopa.interop.signalhub.updater.model.ConsumerEServiceDto;
import reactor.core.publisher.Mono;

public interface ConsumerService {

    Mono<ConsumerEServiceDto> updateConsumer(AgreementEventDto agreementEventDto);



}
