(function () {
  const translations = {
    en: {
      title: "Potion Client — A Meteor-based client extended with addon scripting",
      metaDescription: "Potion Client is an open-source Minecraft utility client based on Meteor Client. Extend it yourself with BeanShell/Python addon scripting.",
      navDownload: "Download",
      navFeatures: "Features",
      navAddons: "Addons",
      navForum: "Share Addons",
      shareAddonBtn: "Share your addon →",
      heroTagline: "Based on Meteor Client — extend it yourself with BeanShell/Python addon scripting.",
      heroDownloadBtn: "Download Potion Client",
      heroAddonsBtn: "See Addons (Python supported)",
      featuresTitle: "Features",
      feature1Title: "Scripting Addons",
      feature1Desc: "Write your own modules and commands with lightweight BeanShell (.bsh), or install the Python Addon for real Python (.py) scripting.",
      feature2Title: "Potion Tab",
      feature2Desc: "A dedicated Potion tab in the ClickGUI lets you enable/disable addons, create new ones, and open the addons folder.",
      feature3Title: "Module Control API",
      feature3Desc: "Addon scripts can toggle other modules and query their state, so you can freely combine and extend existing modules.",
      feature4Title: "Built on Meteor Client",
      feature4Desc: "Forked from the real Meteor Client source — every familiar Combat, Movement, Render, and World module still works.",
      downloadTitle: "Download",
      downloadClientDesc: "Required · the base client, with BeanShell scripting built in",
      downloadAddonDesc: "Optional · drop it in mods/ alongside potion-client to enable .py addons (bundles the GraalPy runtime, larger file)",
      downloadBtn: "Download .jar",
      downloadNote: "Install: put the downloaded .jar file(s) into your Fabric Loader <code>.minecraft/mods</code> folder and launch the game.",
      addonsTitle: "Writing Addons",
      addonsNote: "Drop addon files into <code>meteor-client/addons/</code> in your client folder. You can enable/disable them from the in-game Potion tab.",
      bshCode: "// example.bsh — BeanShell (built in, always works)\nmyFeatureHandler() {\n    run() {\n        po.chat(\"Hello from my addon!\");\n    }\n    return this;\n}\n\npo.set(\"Potion\", \"my-feature\", (Runnable) myFeatureHandler());\n",
      pyCode: "# example.py — Python (works once the Python Addon is installed)\nimport Potion as po\n\ndef hello():\n    po.chat(\"Hello from a Python addon!\")\n\npo.set(\"Potion\", \"python-hello\", hello)\n",
      footerBasedOn: 'Potion Client is based on <a href="https://meteorclient.com" target="_blank" rel="noopener">Meteor Client</a>, licensed under GPL-3.0.',
      footerSource: "Source on GitHub",
    },
    ko: {
      title: "Potion Client — 애드온 스크립팅으로 확장하는 Meteor 기반 클라이언트",
      metaDescription: "Potion Client는 Meteor Client 기반의 오픈소스 Minecraft 유틸리티 클라이언트입니다. BeanShell/Python 애드온 스크립팅으로 직접 기능을 확장할 수 있습니다.",
      navDownload: "다운로드",
      navFeatures: "기능",
      navAddons: "애드온",
      navForum: "애드온 공유",
      shareAddonBtn: "내가 만든 애드온 공유하기 →",
      heroTagline: "Meteor Client 기반, 애드온(BeanShell/Python) 스크립팅으로 기능을 직접 확장하는 유틸리티 클라이언트",
      heroDownloadBtn: "Potion Client 다운로드",
      heroAddonsBtn: "애드온(Python 지원) 보기",
      featuresTitle: "기능",
      feature1Title: "스크립팅 애드온",
      feature1Desc: "BeanShell(.bsh)로 가볍게, 또는 Python Addon을 설치하면 진짜 Python(.py)으로도 나만의 모듈/명령어를 직접 만들 수 있습니다.",
      feature2Title: "Potion 탭",
      feature2Desc: "ClickGUI에 추가된 전용 Potion 탭에서 애드온을 켜고 끄고, 새로 만들고, 폴더를 바로 열 수 있습니다.",
      feature3Title: "모듈 제어 API",
      feature3Desc: "애드온 스크립트에서 다른 모듈을 켜고 끄거나 상태를 조회할 수 있어 기존 모듈과 자유롭게 조합해 확장할 수 있습니다.",
      feature4Title: "Meteor Client 기반",
      feature4Desc: "실제 Meteor Client 소스를 포크해서 만들어졌습니다 — Combat, Movement, Render, World 등 익숙한 모든 모듈을 그대로 사용할 수 있습니다.",
      downloadTitle: "Download",
      downloadClientDesc: "필수 · 기본 클라이언트, BeanShell 스크립팅 기본 내장",
      downloadAddonDesc: "선택 · potion-client와 함께 mods 폴더에 넣으면 .py 애드온 사용 가능 (GraalPy 런타임 포함, 용량 큼)",
      downloadBtn: ".jar 다운로드",
      downloadNote: "설치: 다운로드한 .jar 파일(들)을 Fabric Loader가 설치된 <code>.minecraft/mods</code> 폴더에 넣고 실행하세요.",
      addonsTitle: "애드온 만들기",
      addonsNote: "애드온 파일은 클라이언트 폴더의 <code>meteor-client/addons/</code>에 넣으면 됩니다. 게임 내 Potion 탭에서 켜고 끌 수 있습니다.",
      bshCode: "// example.bsh — BeanShell (기본 내장, 항상 작동)\nmyFeatureHandler() {\n    run() {\n        po.chat(\"Hello from my addon!\");\n    }\n    return this;\n}\n\npo.set(\"Potion\", \"my-feature\", (Runnable) myFeatureHandler());\n",
      pyCode: "# example.py — Python (Python Addon 설치 시 작동)\nimport Potion as po\n\ndef hello():\n    po.chat(\"Hello from a Python addon!\")\n\npo.set(\"Potion\", \"python-hello\", hello)\n",
      footerBasedOn: 'Potion Client is based on <a href="https://meteorclient.com" target="_blank" rel="noopener">Meteor Client</a>, licensed under GPL-3.0.',
      footerSource: "GitHub에서 소스 보기",
    },
  };

  const STORAGE_KEY = "potion-client-lang";

  function detectLang() {
    const saved = localStorage.getItem(STORAGE_KEY);
    if (saved === "en" || saved === "ko") return saved;

    const browserLang = (navigator.language || navigator.userLanguage || "en").toLowerCase();
    return browserLang.startsWith("ko") ? "ko" : "en";
  }

  function applyLang(lang) {
    document.documentElement.lang = lang;

    document.querySelectorAll("[data-i18n]").forEach((el) => {
      const key = el.getAttribute("data-i18n");
      const value = translations[lang][key];
      if (value === undefined) return;

      if (el.tagName === "META") {
        el.setAttribute("content", value);
      } else if (el.tagName === "TITLE") {
        el.textContent = value;
      } else if (el.hasAttribute("data-i18n-html")) {
        el.innerHTML = value;
      } else {
        el.textContent = value;
      }
    });

    const toggle = document.getElementById("lang-toggle");
    if (toggle) toggle.textContent = lang === "ko" ? "EN" : "한국어";
  }

  function setLang(lang) {
    localStorage.setItem(STORAGE_KEY, lang);
    applyLang(lang);
  }

  // These contain HTML (links/tags), so render with innerHTML instead of textContent.
  ["downloadNote", "addonsNote", "footerBasedOn"].forEach((key) => {
    document.querySelectorAll(`[data-i18n="${key}"]`).forEach((el) => el.setAttribute("data-i18n-html", ""));
  });

  let currentLang = detectLang();
  applyLang(currentLang);

  const toggle = document.getElementById("lang-toggle");
  if (toggle) {
    toggle.addEventListener("click", () => {
      currentLang = currentLang === "ko" ? "en" : "ko";
      setLang(currentLang);
    });
  }
})();
