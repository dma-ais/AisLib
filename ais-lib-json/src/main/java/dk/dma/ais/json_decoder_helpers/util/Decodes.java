package dk.dma.ais.json_decoder_helpers.util;

import dk.dma.ais.message.AisMessage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Decodes {
    Class<? extends AisMessage> className();
}
