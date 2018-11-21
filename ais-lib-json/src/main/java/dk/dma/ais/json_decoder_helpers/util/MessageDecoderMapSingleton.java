package dk.dma.ais.json_decoder_helpers.util;

import dk.dma.ais.json_decoder_helpers.message_decoders.AisMessageDecoder;
import dk.dma.ais.message.AisMessage;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;

public class MessageDecoderMapSingleton {
    private Logger logger = LoggerFactory.getLogger(MessageDecoderMapSingleton.class);

    private static MessageDecoderMapSingleton INSTANCE;
    private HashMap<Class<? extends AisMessage>, Class<? extends AisMessageDecoder>> messageDecoderMap;

    public MessageDecoderMapSingleton() {
        getDecoders();
    }

    public static MessageDecoderMapSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MessageDecoderMapSingleton();
        }
        return INSTANCE;
    }

    private void getDecoders() {
        messageDecoderMap = new HashMap<>();

        try {
            Reflections reflections = new Reflections("dk.dma.ais.json_decoder_helpers.message_decoders");
            Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Decodes.class);
            for (Class<?> cls : annotated) {
                Decodes annotatedClass = cls.getAnnotation(Decodes.class);
                messageDecoderMap.put(annotatedClass.className(), (Class<? extends AisMessageDecoder>) cls);
            }
        } catch (ClassCastException e) {
            logger.warn("Someone tried to use a decoder on a class that did not extend AisMessageDecoder");
        }
    }

    public AisMessageDecoder getDecoderForMessage(AisMessage aisMessage) throws DecoderGrabbingException {
        try {

            Class<? extends AisMessageDecoder> cls = messageDecoderMap.get(aisMessage.getClass());
            if (cls == null) {
                String str = aisMessage.getMsgId() + " Message type not supported";
                logger.warn(str);
                throw new DecoderGrabbingException(aisMessage.getMsgId() + " Message type not supported");
            }
            Constructor<? extends AisMessageDecoder> ctor = cls.getConstructor(aisMessage.getClass());
            return ctor.newInstance(aisMessage);

        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.warn("Something went wrong with getting the decoder needed for that AisMessage");
            throw new DecoderGrabbingException(e.getMessage());
        }
    }
}
