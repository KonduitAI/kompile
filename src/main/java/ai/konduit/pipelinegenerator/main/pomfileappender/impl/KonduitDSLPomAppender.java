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

public class KonduitDSLPomAppender implements PomFileAppender {
    @Override
    public DependencyType dependencyType() {
        return DependencyType.KONDUIT_SERVING_DSL;
    }

    @Override
    public List<String> classesToAppend() {
        return Arrays.asList(
                "ai.konduit.serving.pipeline.impl.pipeline.SequencePipelineExecutor",
                "ai.konduit.serving.pipeline.util.ObjectMappers",
                "ai.konduit.serving.pipeline.registry.PipelineRegistry"
        );
    }

    @Override
    public InitializeType initializeType() {
        return InitializeType.RUNTIME;
    }
}
