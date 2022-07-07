/*
 * Copyright (c) 2022 Konduit K.K.
 *
 *     This program and the accompanying materials are made available under the
 *     terms of the Apache License, Version 2.0 which is available at
 *     https://www.apache.org/licenses/LICENSE-2.0.
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *     WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *     License for the specific language governing permissions and limitations
 *     under the License.
 *
 *     SPDX-License-Identifier: Apache-2.0
 */

package ai.konduit.pipelinegenerator.main.pomfileappender.impl;

import ai.konduit.pipelinegenerator.main.pomfileappender.PomFileAppender;

import java.util.Arrays;
import java.util.List;

public class ApacheCommonsPomFileAppender implements PomFileAppender {
    @Override
    public DependencyType dependencyType() {
        return DependencyType.APACHE_COMMONS;
    }

    @Override
    public List<String> classesToAppend() {
        return Arrays.asList(
                "org.apache.commons.io.FileUtils",
                "org.apache.commons.io.Charsets",
                "org.apache.commons.io.FilenameUtils",
                "org.apache.commons.io.IOUtils"
        );
    }

    @Override
    public InitializeType initializeType() {
        return InitializeType.BUILD_TIME;
    }
}
