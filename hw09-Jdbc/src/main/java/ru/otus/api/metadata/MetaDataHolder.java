package ru.otus.api.metadata;

import ru.otus.metadata.MetaData;

public interface MetaDataHolder {
    void saveObjectMetadata(Class className);

    MetaData getObjectMetaData(Class className);
}
