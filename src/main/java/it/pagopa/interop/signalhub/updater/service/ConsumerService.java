package it.pagopa.interop.signalhub.updater.service;

import it.pagopa.interop.signalhub.updater.model.AgreementEventDto;
import it.pagopa.interop.signalhub.updater.model.ConsumerEServiceDto;


public interface ConsumerService {

    ConsumerEServiceDto updateConsumer(AgreementEventDto agreementEventDto);



}
