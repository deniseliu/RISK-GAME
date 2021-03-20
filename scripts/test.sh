#!/bin/bash
./gradlew build || exit 1
./gradlew cloverGenerateReport || exit 1
./gradlew cloverAggregateReports || exit 1
scripts/coverage_summary.sh
ls -l /
ls -l /coverage-out/
cp -r build/reports/clover/* /coverage-out/ || exit 1

