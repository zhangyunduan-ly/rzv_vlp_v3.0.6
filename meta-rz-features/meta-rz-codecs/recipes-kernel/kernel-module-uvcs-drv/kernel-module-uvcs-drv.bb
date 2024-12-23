DESCRIPTION = "Kernel module of UVCS"
LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = " \
    file://include/GPL-COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
    file://src/lkm/GPL-COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
    file://src/core/GPL-COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
    file://src/cmn/GPL-COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
    file://include/MIT-COPYING;md5=fea016ce2bdf2ec10080f69e9381d378 \
    file://src/lkm/MIT-COPYING;md5=fea016ce2bdf2ec10080f69e9381d378 \
    file://src/core/MIT-COPYING;md5=fea016ce2bdf2ec10080f69e9381d378 \
    file://src/cmn/MIT-COPYING;md5=fea016ce2bdf2ec10080f69e9381d378 \
"
inherit module
require include/rz-modules-common.inc
PR = "r0"

COMPATIBLE_MACHINE_rzg2l = "(r9a07g044l|r9a07g054l)"
COMPATIBLE_MACHINE_rzg2h = "(r8a774a1|r8a774b1|r8a774c0|r8a774e1)"

SRC_URI_rzg2l = "${@oe.utils.conditional('USE_VIDEO_OMX', '1', 'file://uvcs_kernel_package.tar.bz2', '', d)}"
SRC_URI_rzg2h = "${@oe.utils.conditional('USE_VIDEO_OMX', '1', 'file://RCG3VUDRL4101ZDO.tar.bz2', '', d)}"

SRC_URI_append = " \
	file://0001-Fixing-build-error-kernel-module-uvcs.patch \
	file://0002-kernel-module-uvcs-drv-fix-built-error-with-kernel-v.patch \
"

SRC_URI_append_rzg2l = " \
	file://0003-Add-SMC-calls-for-uvcs-kernel.patch \
"

S_rzg2l = "${WORKDIR}/uvcs_kernel_package"
S_rzg2h = "${WORKDIR}/RCG3VUDRL4101ZDO"

EXTRA_OEMAKE = "KERNELDIR=${STAGING_KERNEL_BUILDDIR}"
EXTRA_OEMAKE += "CROSS_COMPILE=${CROSS_COMPILE}"

B="${S}/src/makefile"

includedir = "${RENESAS_DATADIR}/include"

do_fetch[file-checksums] = ""

do_compile_prepend() {
    export UVCS_SRC="${S}/src"
    export UVCS_INC="${S}"
    export VCP4_SRC="${S}/src"
}

# Build UVCS kernel module without suffix
KERNEL_MODULE_PACKAGE_SUFFIX = ""

do_install() {
    # Create destination directory
    install -d ${D}/lib/modules/${KERNEL_VERSION}/extra/
    install -d ${D}/${includedir}/

    # Install kernel module
    install -m 644 ${B}/uvcs_drv.ko ${D}/lib/modules/${KERNEL_VERSION}/extra/

    # Install module symbol file
    install -m 644 ${B}/Module.symvers ${STAGING_KERNEL_BUILDDIR}/UVCS.symvers

    # Install shared header file
    install -m 644 ${S}/include/uvcs_ioctl.h ${D}/${includedir}/
}

# Clean up the module symbol file
CLEANFUNCS = "module_clean_symbol"

module_clean_symbol() {
    rm -f ${STAGING_KERNEL_BUILDDIR}/UVCS.symvers
}

PACKAGES = " \
    ${PN} \
    ${PN}-sstate \
"

FILES_${PN}-sstate = " \
    ${includedir}/uvcs_ioctl.h \
"

# KERNEL_MODULE_AUTOLOAD = "${@oe.utils.conditional('USE_VIDEO_OMX', '1', 'uvcs_drv', '', d)}"

KERNEL_MODULE_AUTOLOAD = "uvcs_drv"
