#!/usr/bin/python3

import matplotlib.pyplot as plt
import subprocess
from datetime import datetime
import json
import math

def check_and_set_subplots(fig, container_names):
    if len(fig.axes) != len(container_names):
        print("Resetting figure axes:", container_names)
        fig.clear()

        gs = fig.add_gridspec(math.ceil(len(container_names) / 2), 2)
        print("")
        for i in range(math.ceil(len(container_names) / 2)):
            for j in range(2):
                if (i * 2 + j) >= len(container_names):
                    continue
                fig.add_subplot(gs[i, j])


def plot_data(fig, data_to_plot):
    container_names = list(data_to_plot.keys())
    check_and_set_subplots(fig, container_names)

    minDate = None
    maxDate = None

    for container_name in data_to_plot:
        minDate = min(data_to_plot[container_name]["time"]) if minDate is None else min(minDate, min(
            data_to_plot[container_name]["time"]))
        maxDate = max(data_to_plot[container_name]["time"]) if minDate is None else max(minDate, max(
            data_to_plot[container_name]["time"]))

    for i in range(math.ceil(len(container_names) / 2)):
        for j in range(2):
            if i * 2 + j >= len(container_names):
                continue
            container_name = container_names[i * 2 + j]

            ax = fig.axes[i * 2 + j]
            ax.set_title(container_name)

            if minDate != maxDate:
                ax.set_xlim([minDate, maxDate])

            ax.set_ylim([0, 100.5])
            ax.grid(True)
            ax.plot(data_to_plot[container_name]["time"], data_to_plot[container_name]["memPerc"], color="red")
            fig.autofmt_xdate()

    plt.pause(0.01)
    plt.show()


def main():

    data_to_plot = {}

    plt.ion()
    fig = plt.figure()
    print("Created figure")

    while True:
        process = subprocess.Popen(["docker", "stats", "--format", "json", "--no-stream"], stdout=subprocess.PIPE)

        for byteLine in process.stdout:
            strLine = str(byteLine)

            firstSplit = strLine.split("{")
            if len(firstSplit) < 2:
                continue

            jsonLine = json.loads("{" + firstSplit[1].split("}")[0] + "}")

            if jsonLine["Name"] == "--":
                continue

            if jsonLine["Name"] not in data_to_plot:
                data_to_plot[jsonLine["Name"]] = {"time": [], "memPerc": []}

            data_to_plot[jsonLine["Name"]]["time"].append(datetime.now())
            data_to_plot[jsonLine["Name"]]["memPerc"].append(float(jsonLine["MemPerc"].replace("%", "")))

        plot_data(fig, data_to_plot)


if __name__ == "__main__":
    main()
