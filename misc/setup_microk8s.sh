#!/bin/bash

# install microk8s
install_microk8s()
{
    sudo snap install microk8s --channel=1.17/stable --classic
    microk8s.status --wait-ready

    # do not enable rbac as it may interfere with registry component due to some upstream bug
    microk8s.enable dns storage registry rbac
    microk8s.status
}

# install kubectl and kustomize binaries
# at this moment (2020/01/13 1.14 microk8s has some issue with registry addon)
install_utils()
{
    curl -LO https://storage.googleapis.com/kubernetes-release/release/v1.17.0/bin/linux/amd64/kubectl
    sudo install kubectl /usr/local/bin

    curl -LO https://github.com/kubernetes-sigs/kustomize/releases/download/kustomize%2Fv3.5.3/kustomize_v3.5.3_linux_amd64.tar.gz
    tar zxf kustomize_v3.5.3_linux_amd64.tar.gz
    sudo install kustomize /usr/local/bin

    # if you want to setup kubectl to work with microk8s, do the following:
    # microk8s.config > $HOME/.kube/config
}

distro=$(awk -F= '/^NAME/{print $2}' /etc/os-release)

if [ "$distro" != "\"Ubuntu\"" ]; then
    echo microk8s does not support Linux distro $distro
    exit -1
fi

echo installing microk8s in 5 seconds, press ctrl-C to abort
sleep 5

install_microk8s
install_utils
