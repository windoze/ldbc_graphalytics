/*
 * Copyright 2015 - 2017 Atlarge Research Team,
 * operating at Technische Universiteit Delft
 * and Vrije Universiteit Amsterdam, the Netherlands.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package science.atlarge.graphalytics.execution;

import science.atlarge.graphalytics.domain.graph.FormattedGraph;
import science.atlarge.graphalytics.report.result.BenchmarkMetrics;
import science.atlarge.graphalytics.domain.benchmark.BenchmarkRun;

/**
 * The common interface for any platform that implements the Graphalytics benchmark suite. It
 * defines the API that must be provided by a platform to be compatible with the Graphalytics
 * benchmark driver. The driver uses the {@link #loadGraph(FormattedGraph) loadGraph} and
 * {@link #deleteGraph(FormattedGraph) deleteGraph} functions to ensure the right graphs are loaded,
 * and uses {@link #run(BenchmarkRun) run} to trigger the executing of various algorithms on each graph.
 *
 * @author Mihai Capotă
 * @author Tim Hegeman
 * @author Wing Lung Ngai
 */
public interface Platform {


    /**
     * The benchmark suite verifies that the platform and the environment are properly set up
     * based on the prerequisites defined in the platform driver.
     */
    void verifySetup() throws Exception;


    /**
     * The platform converts the “formatted data" into any platform-specific data format and
     * loads a graph dataset into a storage system, which can be either a local file system,
     * a share file system or a distributed file system.
     *
     * The platform driver must ensure that this dataset remains available for multiple calls to
     * {@link #run(BenchmarkRun) executeAlgorithmOnGraph}, until the removal of the graph
     * is triggered using {@link #deleteGraph(FormattedGraph) deleteGraph}.
     *
     * @param formattedGraph information on the graph to be uploaded
     * @throws Exception if any exception occurred during the upload
     */
    void loadGraph(FormattedGraph formattedGraph) throws Exception;


    /**
     * The platform requests computation resources from the cluster environment and
     * makes the background applications ready.
     * @param benchmarkRun job specification of a benchmark run.
     */
    void prepare(BenchmarkRun benchmarkRun) throws Exception;


    /**
     * The platform configures the benchmark run, with regard to real-time cluster deployment information,
     * e.g., input directory, output directory, and log directory.
     *
     * @param benchmarkRun job specification of a benchmark run.
     */
    void startup(BenchmarkRun benchmarkRun) throws Exception;

    /**
     * The platform runs a graph-processing job as defined in the benchmark run.
     * The graph-processing job must complete within the time-out duration, or the benchmark run will fail.
     *
     * @param benchmarkRun job specification of a benchmark run.
     * @throws PlatformExecutionException if any exception occurred during the execution of the algorithm.
     */
    void run(BenchmarkRun benchmarkRun) throws PlatformExecutionException;


    /**
     * The platform reports the benchmark metrics, and make the environment ready again for the next benchmark run.
     *
     * @param benchmarkRun job specification of a benchmark run..
     * @return performance metrics measued for this benchmark run.
     */
    BenchmarkMetrics finalize(BenchmarkRun benchmarkRun) throws Exception;

    /**
     * @param benchmarkRun
     */
    void terminate(BenchmarkRun benchmarkRun) throws Exception;

    /**
     * The platform unloads a graph dataset from the storage system,
     * as part of the cleaning up process after all benchmark runs
     * on that graph dataset have been completed.
     *
     * @param formattedGraph information on the graph to be uploaded
     */
    void deleteGraph(FormattedGraph formattedGraph) throws Exception;


    /**
     * A unique identifier for the platform, used to name benchmark results, etc.
     * This should be the same as the platform name used to compile and run the benchmark
     * for this platform, for consistency.
     *
     * @return the unique name of the platform.
     */
    String getPlatformName();

}
