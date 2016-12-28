package com.restdude.domain.base.binding;

import com.restdude.domain.base.model.IEmbeddableManyToManyId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

public class StringToEmbeddableManyToManyIdConverterFactory implements ConverterFactory<String, IEmbeddableManyToManyId> {

    @Override
    public <T extends IEmbeddableManyToManyId> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEmbeddableManyToManyIdConverter<T>(targetType);
    }

    @SuppressWarnings("rawtypes")
    private final class StringToEmbeddableManyToManyIdConverter<T extends IEmbeddableManyToManyId> implements Converter<String, T> {

        private Class targetType;

        public StringToEmbeddableManyToManyIdConverter(Class<?> targetType) {
            this.targetType = targetType;
        }

        @Override
        public T convert(String id) {
            T object;
            try {
                object = (T) targetType.newInstance();
                object.init(id);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(new StringBuffer("Failed to deserialize with id: ").append(id).append(", class: ").append(targetType.toString()).toString(), e);
            }
            return object;
        }
    }
}