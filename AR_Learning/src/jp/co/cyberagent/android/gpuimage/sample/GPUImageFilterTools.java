/*
 * Copyright (C) 2012 CyberAgent
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.co.cyberagent.android.gpuimage.sample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import jp.co.cyberagent.android.gpuimage.*;

import java.util.LinkedList;
import java.util.List;

public class GPUImageFilterTools {
    public static void showDialog(final Context context,
            final OnGpuImageFilterChosenListener listener) {
        final FilterList filters = new FilterList();
        filters.addFilter("Sobel + Dilation 1", FilterType.FILTER_GROUP1);
        filters.addFilter("Sobel + Dilation 2", FilterType.FILTER_GROUP2);
        filters.addFilter("Sobel + Dilation 3", FilterType.FILTER_GROUP3);
        filters.addFilter("Sobel + Dilation 4", FilterType.FILTER_GROUP4);
        filters.addFilter("Sobel + Dilation 5", FilterType.FILTER_GROUP5);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose Dilation Level");
        builder.setItems(filters.names.toArray(new String[filters.names.size()]),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int item) {
                        listener.onGpuImageFilterChosenListener(
                                createFilterForType(filters.filters.get(item)));
                    }
                });
        builder.create().show();
    }

    private static GPUImageFilter createFilterForType(final FilterType type) {
        switch (type) {
            case FILTER_GROUP1:
                List<GPUImageFilter> filters = new LinkedList<GPUImageFilter>();
                filters.add(new GPUImageSobelEdgeDetection());
                filters.add(new GPUImageDilationFilter(1));
                return new GPUImageFilterGroup(filters);
            case FILTER_GROUP2:
                filters = new LinkedList<GPUImageFilter>();
                filters.add(new GPUImageSobelEdgeDetection());
                filters.add(new GPUImageDilationFilter(2));
                return new GPUImageFilterGroup(filters);
            case FILTER_GROUP3:
                filters = new LinkedList<GPUImageFilter>();
                filters.add(new GPUImageSobelEdgeDetection());
                filters.add(new GPUImageDilationFilter(3));
                return new GPUImageFilterGroup(filters);
            case FILTER_GROUP4:
                filters = new LinkedList<GPUImageFilter>();
                filters.add(new GPUImageSobelEdgeDetection());
                filters.add(new GPUImageDilationFilter(4));
                return new GPUImageFilterGroup(filters);
            case FILTER_GROUP5:
                filters = new LinkedList<GPUImageFilter>();
                filters.add(new GPUImageSobelEdgeDetection());
                filters.add(new GPUImageDilationFilter(8));
                return new GPUImageFilterGroup(filters);
            default:
                throw new IllegalStateException("No filter of that type!");
        }

    }

    public interface OnGpuImageFilterChosenListener {
        void onGpuImageFilterChosenListener(GPUImageFilter filter);
    }

    private enum FilterType {
        FILTER_GROUP1, FILTER_GROUP2, FILTER_GROUP3, FILTER_GROUP4, FILTER_GROUP5
    }

    private static class FilterList {
        public List<String> names = new LinkedList<String>();
        public List<FilterType> filters = new LinkedList<FilterType>();

        public void addFilter(final String name, final FilterType filter) {
            names.add(name);
            filters.add(filter);
        }
    }

    public static class FilterAdjuster {
        private final Adjuster<? extends GPUImageFilter> adjuster;

        public FilterAdjuster(final GPUImageFilter filter) {
            if (filter instanceof GPUImageFilterGroup) {
                adjuster = new GroupAdjuster().filter(filter);
            } else {

                adjuster = null;
            }
        }

        public void adjust(final int percentage) {
            if (adjuster != null) {
                adjuster.adjust(percentage);
            }
        }

        private abstract class Adjuster<T extends GPUImageFilter> {
            private T filter;

            @SuppressWarnings("unchecked")
            public Adjuster<T> filter(final GPUImageFilter filter) {
                this.filter = (T) filter;
                return this;
            }

            public T getFilter() {
                return filter;
            }

            public abstract void adjust(int percentage);

            protected float range(final int percentage, final float start, final float end) {
                return (end - start) * percentage / 100.0f + start;
            }
        }

        private class GroupAdjuster extends Adjuster<GPUImageFilterGroup> {
            @Override
            public void adjust(final int percentage) {
                GPUImage3x3TextureSamplingFilter filterSobel = (GPUImage3x3TextureSamplingFilter) getFilter().getMergedFilters().get(1);
                filterSobel.setLineSize(range(percentage, 0.0f, 10.0f));
            }
        }
    }
}
