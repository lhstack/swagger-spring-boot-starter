/*
 *
 *  Copyright 2015-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */
package springfox.documentation.schema;

import org.springframework.util.ClassUtils;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public class ClassSupport {
  private ClassSupport() {
    throw new UnsupportedOperationException();
  }

  public static Optional<Class<?>> classByName(String className) {
    return classByName(className, ClassUtils.getDefaultClassLoader());
  }

  public static Optional<Class<?>> classByName(String className, ClassLoader classLoader) {
    try {
      return of(ClassUtils.forName(className, classLoader));
    } catch (ClassNotFoundException e) {
      return empty();
    }
  }
}
