LIC_FILES_CHKSUM = "file://LICENSE.md;md5=1fb5dca04b27614d6d04abca6f103d8d"
LICENSE="BSD-3-Clause"
PV = "1.06+git${SRCPV}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

FLASH_WRITER_URL = "git://github.com/zhangyunduan-ly/rzg2_flash_writer_ly"
BRANCH = "develop-v2l-ly"

SRC_URI = "${FLASH_WRITER_URL};branch=${BRANCH}"
SRCREV = "708fe524315144b308c05bf3de140536643e1a6e"

inherit deploy
#require include/provisioning.inc

S = "${WORKDIR}/git"
PMIC_BUILD_DIR = "${S}/build_pmic"

do_compile() {
	if [ "${MACHINE}" = "smarc-rzg2l" ]; then
		BOARD="RZG2L_SMARC";
		PMIC_BOARD="RZG2L_SMARC_PMIC";
	elif [ "${MACHINE}" = "rzg2l-dev" ]; then
		BOARD="RZG2L_15MMSQ_DEV";
	elif [ "${MACHINE}" = "smarc-rzg2lc" ]; then
			BOARD="RZG2LC_SMARC";
	elif [ "${MACHINE}" = "rzg2lc-dev" ]; then
		BOARD="RZG2LC_DEV";
	elif [ "${MACHINE}" = "smarc-rzg2ul" ]; then
		BOARD="RZG2UL_SMARC";
	elif [ "${MACHINE}" = "rzg2ul-dev" ]; then
		BOARD="RZG2UL_TYPE1_DEV";
	elif [ "${MACHINE}" = "smarc-rzv2l" ]; then
		BOARD="RZV2L_SMARC";
		PMIC_BOARD="RZV2L_SMARC_PMIC";
	elif [ "${MACHINE}" = "ly-rzv2l" ]; then
		BOARD="RZV2L_LY";
		PMIC_BOARD="RZV2L_LY_PMIC";
	elif [ "${MACHINE}" = "rzv2l-dev" ]; then
		BOARD="RZV2L_15MMSQ_DEV";
	fi
	cd ${S}

	oe_runmake BOARD=${BOARD}

	if [ "${PMIC_SUPPORT}" = "1" ]; then
		oe_runmake OUTPUT_DIR=${PMIC_BUILD_DIR} clean;
		oe_runmake BOARD=${PMIC_BOARD} OUTPUT_DIR=${PMIC_BUILD_DIR};
	fi
}

do_install[noexec] = "1"

do_deploy() {
        install -d ${DEPLOYDIR}
        install -m 644 ${S}/AArch64_output/*.mot ${DEPLOYDIR}
        if [ "${PMIC_SUPPORT}" = "1" ]; then
        	install -m 644 ${PMIC_BUILD_DIR}/*.mot ${DEPLOYDIR}
	fi
}
PARALLEL_MAKE = "-j 1"
addtask deploy after do_compile
