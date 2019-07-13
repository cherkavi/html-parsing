package com.cherkashyn.vitalii.whore.interfaces.elementsaver;

import com.cherkashyn.vitalii.whore.domain.WhoreElement;
import com.cherkashyn.vitalii.whore.exceptions.StorageException;
import com.cherkashyn.vitalii.whore.exceptions.ValidationException;

public interface ElementSaver {
	void saveElement(WhoreElement element) throws ValidationException, StorageException;
}
