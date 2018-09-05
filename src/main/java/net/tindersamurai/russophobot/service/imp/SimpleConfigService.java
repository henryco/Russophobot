package net.tindersamurai.russophobot.service.imp;

import lombok.extern.slf4j.Slf4j;
import net.tindersamurai.russophobot.mvc.data.entity.ConfigProp;
import net.tindersamurai.russophobot.mvc.data.repository.ConfigPropRepository;
import net.tindersamurai.russophobot.service.IConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Slf4j @Transactional
public class SimpleConfigService implements IConfigService {

	private final ConfigPropRepository propRepository;

	@Autowired
	public SimpleConfigService(ConfigPropRepository propRepository) {
		this.propRepository = propRepository;
	}

	@Override
	public String getProp(String name) {
		try {
			return propRepository.getOne(name).getValue();
		} catch (Exception e) {
			log.error("Cannot find prop: {}", name, e);
			return null;
		}
	}

	@Override
	public void saveProp(String name, String value) {
		try {
			propRepository.saveAndFlush(new ConfigProp(name, value));
		} catch (Exception e) {
			log.error("Cannot save prop: {}, val: {}", name, value, e);
		}
	}

	@Override
	public void removeProp(String name) {
		if (name == null) {
			log.warn("Prop id cannot be null");
			return;
		}
		propRepository.deleteById(name);
	}

	@Override
	public boolean isPropExists(String name) {
		if (name == null) {
			log.warn("Prop id cannot be null");
			return false;
		}
		return propRepository.existsById(name);
	}
}