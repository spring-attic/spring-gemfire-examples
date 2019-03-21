/*
 *  Copyright 2017 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.data.gemfire.examples.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Properties;

/**
 * The {@link Version} class outputs the current version of the Spring GemFire Examples project to Standard Out.
 *
 * @author John Blum
 * @see java.io.File
 * @see java.util.Properties
 * @since 2.0.0
 */
public class Version {

	private static final String DEFAULT_VERSION = "2.0.0.RC2";
	private static final String GRADLE_PROPERTIES_FILE_NAME = "gradle.properties";
	private static final String VERSION_PROPERTY = "version";

	public static void main(String[] args) {
		System.out.printf("Spring GemFire Examples version %s%n; Copyright (c) %d",
			resolveVersion(), LocalDate.now().getYear());
	}

	private static String resolveVersion() {

		return Optional.of(new File(GRADLE_PROPERTIES_FILE_NAME))
			.filter(File::exists)
			.map(file -> {
				try {
					Properties properties = new Properties();
					properties.load(new FileReader(file));
					return properties.getProperty(VERSION_PROPERTY);
				}
				catch (IOException ignore) {
					return null;
				}
			})
			.orElse(DEFAULT_VERSION);
	}
}
