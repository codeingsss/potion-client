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
      downloadNativeDesc: "Optional · drop it in mods/ alongside potion-client to enable .dll/.so/.dylib (C/C++, JNI) addons. Just a small opt-in gate jar, no bundled runtime — native addons run completely unsandboxed.",
      downloadBtn: "Download .jar",
      downloadNote: "Install: put the downloaded .jar file(s) into your Fabric Loader <code>.minecraft/mods</code> folder and launch the game.",
      addonsTitle: "Writing Addons",
      addonsNote: "Drop addon files into <code>meteor-client/addons/</code> in your client folder. You can enable/disable them from the in-game Potion tab.",
      bshCode: "// example.bsh — BeanShell (built in, always works)\nmyFeatureHandler() {\n    run() {\n        po.chat(\"Hello from my addon!\");\n    }\n    return this;\n}\n\npo.set(\"Potion\", \"my-feature\", (Runnable) myFeatureHandler());\n",
      pyCode: "# example.py — Python (works once the Python Addon is installed)\nimport Potion as po\n\ndef hello():\n    po.chat(\"Hello from a Python addon!\")\n\npo.set(\"Potion\", \"python-hello\", hello)\n",
      cCode: "// example.c — Native (compile to a .dll/.so, works once the Native Addon is installed)\n#include <jni.h>\n\n// Called every tick while the module is enabled — Potion (Java) calls this native function directly.\nvoid my_tick(JNIEnv *env, jclass cls, jlong handle) {\n    // ... your native code here ...\n}\n\n// Called automatically the moment your .dll/.so is loaded.\nJNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {\n    JNIEnv *env;\n    (*vm)->GetEnv(vm, (void **)&env, JNI_VERSION_1_8);\n\n    jclass cls = (*env)->FindClass(env, \"meteordevelopment/meteorclient/scripting/PotionNative\");\n\n    // Bind Java's \"invokeTick\" native method to our C function (this is what makes Java -> native calls work).\n    JNINativeMethod methods[] = { {\"invokeTick\", \"(J)V\", (void *) my_tick} };\n    (*env)->RegisterNatives(env, cls, methods, 1);\n\n    // Register a module named \"my-feature\"; handle=1 identifies it in my_tick.\n    jmethodID registerModule = (*env)->GetStaticMethodID(env, cls, \"registerModule\",\n        \"(Ljava/lang/String;Ljava/lang/String;J)V\");\n    (*env)->CallStaticVoidMethod(env, cls, registerModule,\n        (*env)->NewStringUTF(env, \"Potion\"), (*env)->NewStringUTF(env, \"my-feature\"), (jlong) 1);\n\n    return JNI_VERSION_1_8;\n}\n",
      footerBasedOn: 'Potion Client is based on <a href="https://meteorclient.com" target="_blank" rel="noopener">Meteor Client</a>, licensed under GPL-3.0.',
      footerSource: "Source on GitHub",

      apiTitle: "po API Reference",
      apiIntro: 'Every addon script gets a "po" variable with the functions below. BeanShell uses camelCase names, Python uses snake_case.',
      apiColBsh: "BeanShell (.bsh)",
      apiColPy: "Python (.py)",
      apiColDesc: "Description",
      apiSet: "Registers a module. If category matches an existing one it's added there, otherwise a new category is created. func is called every tick while the module is enabled.",
      apiCommand: 'Registers a "." command (no arguments).',
      apiCommandArgs: "Registers a command whose handler receives the trailing text as a string argument (empty string if none given).",
      apiJava: "Runs a string of Java code immediately (via BeanShell) and returns its result.",
      apiChat: "Shows a message in the in-game chat.",
      apiError: "Shows a red error message in chat.",
      apiLog: "Writes a message to the log file.",
      apiIp: 'Address of the server you\'re currently connected to ("singleplayer" if none).',
      apiSingleplayer: "Whether you're in singleplayer.",
      apiUsername: "Your current player name.",
      apiHealth: "Current health / max health.",
      apiHunger: "Hunger level.",
      apiPos: "Player coordinates.",
      apiRot: "Player look direction.",
      apiGamemode: "Current game mode.",
      apiDimension: "Current dimension (world) ID.",
      apiPing: "Server ping in ms.",
      apiFps: "Current client FPS.",
      apiTps: "Server tick rate.",
      apiState: "Player state (sneaking / sprinting / on ground / submerged in water).",
      apiXp: "Experience level.",
      apiHeldItem: "ID of the item in your main hand.",
      apiWorldTime: "World time (ticks).",
      apiWeather: "Whether it's raining / thundering.",
      apiDifficulty: "Current difficulty.",
      apiModuleActive: "Checks whether another module is currently enabled.",
      apiModuleToggle: "Toggles/enables/disables another module by name.",
      apiCallbackTitle: "BeanShell Callback Pattern",
      apiCallbackNote: "BeanShell can't pass a bare function as a value, so define a nested function with the same name as the interface method (run/accept) and return that scope.",
      apiCallbackCode: "myHandler() {\n    run() {                 // for Runnable (set, command)\n        po.chat(\"Hi!\");\n    }\n    return this;\n}\npo.set(\"Potion\", \"my-feature\", (Runnable) myHandler());\n\nmyArgHandler() {\n    accept(args) {           // for Consumer&lt;String&gt; (commandWithArgs)\n        po.chat(\"You said: \" + args);\n    }\n    return this;\n}\npo.commandWithArgs(\"say\", (java.util.function.Consumer) myArgHandler());",
      apiExternalTitle: "Using External Python Packages",
      apiExternalNote: "When the Python Addon is installed, the folder <code>meteor-client/addons/.potion_lib/site-packages/</code> in your client folder is automatically added to the Python path. Install pure-Python packages there (using Python on your own machine) and they'll be importable from addon scripts. Packages that need native/C extensions (e.g. some parts of numpy) may not work due to how GraalPy runs.",
      apiExternalCode: 'pip install --target "C:\\...\\meteor-client\\addons\\.potion_lib\\site-packages" requests',

      apiNativeTitle: "Writing Native (C/C++) Addons",
      apiNativeIntro: 'When the Native Addon is installed, .dll (Windows) / .so (Linux) / .dylib (macOS) files in <code>meteor-client/addons/</code> are loaded as shared libraries. The standard JNI entry point <code>JNI_OnLoad</code> is called once, immediately after loading — from there you can call the functions below to chat/read state, and use <code>registerModule</code>/<code>registerCommand</code> to register real modules and commands. Unlike BeanShell/Python, this is completely unsandboxed native code, so only install addons you trust.',
      apiColJava: "Java method (PotionNative)",
      apiColJni: "JNI signature",
      apiNativeRegisterModule: "Registers a module (category, name, handle). While it's enabled, <code>invokeTick(handle)</code> is called every tick.",
      apiNativeRegisterCommand: 'Registers a "." command (cmd, handle). When it runs, <code>invokeCommand(handle, args)</code> is called.',
      apiNativeInvoke: "Native methods with no Java implementation — bind them with <code>RegisterNatives</code> to make Java → native calls work.",
      apiNativeReturnNote: "Class: <code>meteordevelopment/meteorclient/scripting/PotionNative</code>. Use the JNI call function matching the return type — <code>V</code>→CallStaticVoidMethod, <code>Ljava/lang/String;</code>→CallStaticObjectMethod, <code>Z</code>→CallStaticBooleanMethod, <code>F</code>→CallStaticFloatMethod, <code>I</code>→CallStaticIntMethod, <code>D</code>→CallStaticDoubleMethod, <code>J</code>→CallStaticLongMethod.",
      apiNativeCallbackTitle: "Java → Native Callbacks (RegisterNatives)",
      apiNativeCallbackNote: 'Functions like <code>chat()</code>/<code>x()</code> are all "native → Java". To make Java (Potion) call your C function directly every tick or on command, use JNI\'s <code>RegisterNatives</code> to bind <code>invokeTick</code>/<code>invokeCommand</code> (native methods with no Java implementation) to your C functions, then call <code>registerModule</code>/<code>registerCommand</code>. This gives C/C++ real callback-driven modules/commands, equivalent to BeanShell\'s <code>po.set</code>/<code>po.command</code>.',
      apiNativeCallbackCode: 'void my_tick(JNIEnv *env, jclass cls, jlong handle) {\n    // called every tick while the module is enabled\n}\n\nvoid my_command(JNIEnv *env, jclass cls, jlong handle, jstring args) {\n    // called when the command runs\n}\n\n// inside JNI_OnLoad:\njclass cls = (*env)->FindClass(env, "meteordevelopment/meteorclient/scripting/PotionNative");\n\nJNINativeMethod methods[] = {\n    {"invokeTick",    "(J)V",                      (void *) my_tick},\n    {"invokeCommand", "(JLjava/lang/String;)V",     (void *) my_command},\n};\n(*env)->RegisterNatives(env, cls, methods, 2);\n\njmethodID registerModule = (*env)->GetStaticMethodID(env, cls, "registerModule",\n    "(Ljava/lang/String;Ljava/lang/String;J)V");\n(*env)->CallStaticVoidMethod(env, cls, registerModule,\n    (*env)->NewStringUTF(env, "Potion"), (*env)->NewStringUTF(env, "my-feature"), (jlong) 1);',
      apiNativeCallbackNote2: "Note: <code>RegisterNatives</code> binds per class+signature, not per library — if multiple native addons register <code>invokeTick</code>/<code>invokeCommand</code> at the same time, whichever loads last overwrites the earlier binding. If you need several modules/commands, branch on the <code>handle</code> value inside one addon. Running <code>java()</code> isn't available to native addons since it takes a BeanShell callback.",
      apiNativeBuildTitle: "Compiling",
      apiNativeBuildNote: "You need the <code>jni.h</code>/<code>jni_md.h</code> headers from your JDK's <code>include</code> folder. Drop the resulting .dll/.so/.dylib into <code>meteor-client/addons/</code>.",
      apiNativeBuildCode: '# Windows (MinGW gcc)\ngcc -shared -I"%JAVA_HOME%\\include" -I"%JAVA_HOME%\\include\\win32" -o example.dll example.c\n\n# Linux\ngcc -shared -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" -o example.so example.c\n\n# macOS\nclang -shared -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin" -o example.dylib example.c',
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
      downloadNativeDesc: "선택 · potion-client와 함께 mods 폴더에 넣으면 .dll/.so/.dylib(C/C++, JNI) 애드온 사용 가능. 번들된 런타임은 없고 로딩 허용 여부만 켜는 작은 jar이며, 네이티브 애드온은 샌드박스 없이 완전히 실행됩니다.",
      downloadBtn: ".jar 다운로드",
      downloadNote: "설치: 다운로드한 .jar 파일(들)을 Fabric Loader가 설치된 <code>.minecraft/mods</code> 폴더에 넣고 실행하세요.",
      addonsTitle: "애드온 만들기",
      addonsNote: "애드온 파일은 클라이언트 폴더의 <code>meteor-client/addons/</code>에 넣으면 됩니다. 게임 내 Potion 탭에서 켜고 끌 수 있습니다.",
      bshCode: "// example.bsh — BeanShell (기본 내장, 항상 작동)\nmyFeatureHandler() {\n    run() {\n        po.chat(\"Hello from my addon!\");\n    }\n    return this;\n}\n\npo.set(\"Potion\", \"my-feature\", (Runnable) myFeatureHandler());\n",
      pyCode: "# example.py — Python (Python Addon 설치 시 작동)\nimport Potion as po\n\ndef hello():\n    po.chat(\"Hello from a Python addon!\")\n\npo.set(\"Potion\", \"python-hello\", hello)\n",
      cCode: "// example.c — Native (컴파일해서 .dll/.so, Native Addon 설치 시 작동)\n#include <jni.h>\n\n// 모듈이 켜져있는 동안 매 틱 호출됩니다 — Potion(자바)이 이 네이티브 함수를 직접 호출합니다.\nvoid my_tick(JNIEnv *env, jclass cls, jlong handle) {\n    // ... 여기에 원하는 네이티브 코드 작성 ...\n}\n\n// .dll/.so가 로드되는 순간 자동으로 호출됩니다.\nJNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {\n    JNIEnv *env;\n    (*vm)->GetEnv(vm, (void **)&env, JNI_VERSION_1_8);\n\n    jclass cls = (*env)->FindClass(env, \"meteordevelopment/meteorclient/scripting/PotionNative\");\n\n    // Java의 \"invokeTick\" native 메서드를 위 C 함수와 연결 (이게 있어야 자바 -> 네이티브 호출이 가능함)\n    JNINativeMethod methods[] = { {\"invokeTick\", \"(J)V\", (void *) my_tick} };\n    (*env)->RegisterNatives(env, cls, methods, 1);\n\n    // \"my-feature\"라는 모듈로 등록. handle=1은 my_tick에서 어떤 등록인지 구분하는 값.\n    jmethodID registerModule = (*env)->GetStaticMethodID(env, cls, \"registerModule\",\n        \"(Ljava/lang/String;Ljava/lang/String;J)V\");\n    (*env)->CallStaticVoidMethod(env, cls, registerModule,\n        (*env)->NewStringUTF(env, \"Potion\"), (*env)->NewStringUTF(env, \"my-feature\"), (jlong) 1);\n\n    return JNI_VERSION_1_8;\n}\n",
      footerBasedOn: 'Potion Client is based on <a href="https://meteorclient.com" target="_blank" rel="noopener">Meteor Client</a>, licensed under GPL-3.0.',
      footerSource: "GitHub에서 소스 보기",

      apiTitle: "po API 레퍼런스",
      apiIntro: '모든 애드온 스크립트에서 "po" 변수로 아래 함수들을 쓸 수 있습니다. BeanShell은 camelCase, Python은 snake_case 이름을 씁니다.',
      apiColBsh: "BeanShell (.bsh)",
      apiColPy: "Python (.py)",
      apiColDesc: "설명",
      apiSet: "모듈 등록. category가 기존 이름과 같으면 거기 들어가고, 없으면 새 카테고리 생성. func은 모듈이 켜져있는 동안 매 틱 호출됨.",
      apiCommand: '"." 명령어 등록 (인자 없음).',
      apiCommandArgs: "명령어 등록. 뒤에 붙는 텍스트를 문자열 인자로 받음 (없으면 빈 문자열).",
      apiJava: "문자열로 된 자바 코드를 즉시 실행하고 결과를 반환 (BeanShell로 해석).",
      apiChat: "인게임 채팅으로 메시지 표시.",
      apiError: "빨간색 에러 채팅 메시지 표시.",
      apiLog: "로그 파일에 기록.",
      apiIp: '현재 접속한 서버 주소 (싱글플레이면 "singleplayer").',
      apiSingleplayer: "싱글플레이 여부.",
      apiUsername: "현재 플레이어 닉네임.",
      apiHealth: "현재 체력 / 최대 체력.",
      apiHunger: "배고픔 수치.",
      apiPos: "플레이어 좌표.",
      apiRot: "플레이어 시야각.",
      apiGamemode: "현재 게임모드.",
      apiDimension: "현재 차원(월드) ID.",
      apiPing: "서버 핑(ms).",
      apiFps: "현재 클라이언트 FPS.",
      apiTps: "서버 틱레이트.",
      apiState: "플레이어 상태 (웅크리기/달리기/땅/물속).",
      apiXp: "경험치 레벨.",
      apiHeldItem: "주손에 든 아이템 ID.",
      apiWorldTime: "월드 시간(틱).",
      apiWeather: "비/뇌우 여부.",
      apiDifficulty: "현재 난이도.",
      apiModuleActive: "다른 모듈이 켜져있는지 확인.",
      apiModuleToggle: "다른 모듈을 켜고 끄기 (이름으로 지정).",
      apiCallbackTitle: "BeanShell 콜백 패턴",
      apiCallbackNote: "BeanShell은 함수를 값으로 바로 못 넘기기 때문에, 인터페이스 메서드 이름(run/accept)과 같은 이름의 중첩 함수를 만들고 그 스코프를 반환해야 합니다.",
      apiCallbackCode: "myHandler() {\n    run() {                 // Runnable용 (set, command)\n        po.chat(\"Hi!\");\n    }\n    return this;\n}\npo.set(\"Potion\", \"my-feature\", (Runnable) myHandler());\n\nmyArgHandler() {\n    accept(args) {           // Consumer&lt;String&gt;용 (commandWithArgs)\n        po.chat(\"You said: \" + args);\n    }\n    return this;\n}\npo.commandWithArgs(\"say\", (java.util.function.Consumer) myArgHandler());",
      apiExternalTitle: "외부 Python 패키지 사용",
      apiExternalNote: "Python Addon 설치 시, 클라이언트 폴더의 <code>meteor-client/addons/.potion_lib/site-packages/</code> 폴더가 자동으로 Python 경로에 포함됩니다. 본인 컴퓨터에 설치된 Python으로 순수 Python 패키지를 그 폴더에 설치하면 addon 스크립트에서 바로 import할 수 있습니다. C/네이티브 확장이 필요한 패키지(numpy 일부 등)는 GraalPy 특성상 동작하지 않을 수 있습니다.",
      apiExternalCode: 'pip install --target "C:\\...\\meteor-client\\addons\\.potion_lib\\site-packages" requests',

      apiNativeTitle: "네이티브(C/C++) 애드온 작성법",
      apiNativeIntro: "Native Addon 설치 시, <code>meteor-client/addons/</code>의 .dll(Windows)/.so(Linux)/.dylib(macOS) 파일이 공유 라이브러리로 로드됩니다. 표준 JNI 진입점인 <code>JNI_OnLoad</code>가 로드 즉시 한 번 호출되며, 여기서 아래 함수들을 호출해 채팅/상태 조회는 물론 <code>registerModule</code>/<code>registerCommand</code>로 진짜 모듈·명령어까지 등록할 수 있습니다. BeanShell/Python과 달리 샌드박스가 전혀 없는 네이티브 코드이므로, 신뢰할 수 있는 애드온만 설치하세요.",
      apiColJava: "Java 메서드 (PotionNative)",
      apiColJni: "JNI 시그니처",
      apiNativeRegisterModule: "모듈 등록 (category, name, handle). 모듈이 켜져있는 동안 매 틱 <code>invokeTick(handle)</code>이 호출됨.",
      apiNativeRegisterCommand: '"." 명령어 등록 (cmd, handle). 실행 시 <code>invokeCommand(handle, args)</code>가 호출됨.',
      apiNativeInvoke: "자바 쪽엔 구현이 없는 native 메서드 — <code>RegisterNatives</code>로 직접 바인딩해야 자바 → 네이티브 호출이 가능해짐.",
      apiNativeReturnNote: "클래스: <code>meteordevelopment/meteorclient/scripting/PotionNative</code>. 반환 타입에 맞는 JNI 호출 함수를 쓰세요 — <code>V</code>→CallStaticVoidMethod, <code>Ljava/lang/String;</code>→CallStaticObjectMethod, <code>Z</code>→CallStaticBooleanMethod, <code>F</code>→CallStaticFloatMethod, <code>I</code>→CallStaticIntMethod, <code>D</code>→CallStaticDoubleMethod, <code>J</code>→CallStaticLongMethod.",
      apiNativeCallbackTitle: "자바 → 네이티브 콜백 (RegisterNatives)",
      apiNativeCallbackNote: "<code>chat()</code>/<code>x()</code> 같은 함수들은 전부 \"네이티브 → 자바\" 방향입니다. 반대로 자바(Potion)가 틱마다 또는 명령어 실행 시 여러분의 C 함수를 직접 호출하게 하려면, JNI의 <code>RegisterNatives</code>로 <code>invokeTick</code>/<code>invokeCommand</code>(자바 쪽엔 구현이 없는 native 메서드)를 여러분의 C 함수와 연결한 다음 <code>registerModule</code>/<code>registerCommand</code>를 호출하세요. 이렇게 하면 BeanShell의 <code>po.set</code>/<code>po.command</code>와 동등한, 진짜 콜백 기반 모듈/명령어를 C/C++로 만들 수 있습니다.",
      apiNativeCallbackCode: 'void my_tick(JNIEnv *env, jclass cls, jlong handle) {\n    // 모듈이 켜져있는 동안 매 틱 호출됨\n}\n\nvoid my_command(JNIEnv *env, jclass cls, jlong handle, jstring args) {\n    // 명령어 실행 시 호출됨\n}\n\n// JNI_OnLoad 안에서:\njclass cls = (*env)->FindClass(env, "meteordevelopment/meteorclient/scripting/PotionNative");\n\nJNINativeMethod methods[] = {\n    {"invokeTick",    "(J)V",                      (void *) my_tick},\n    {"invokeCommand", "(JLjava/lang/String;)V",     (void *) my_command},\n};\n(*env)->RegisterNatives(env, cls, methods, 2);\n\njmethodID registerModule = (*env)->GetStaticMethodID(env, cls, "registerModule",\n    "(Ljava/lang/String;Ljava/lang/String;J)V");\n(*env)->CallStaticVoidMethod(env, cls, registerModule,\n    (*env)->NewStringUTF(env, "Potion"), (*env)->NewStringUTF(env, "my-feature"), (jlong) 1);',
      apiNativeCallbackNote2: "주의: <code>RegisterNatives</code>는 클래스+시그니처 단위로 바인딩되므로, 여러 개의 네이티브 애드온이 동시에 <code>invokeTick</code>/<code>invokeCommand</code>를 등록하면 나중에 로드된 애드온이 앞의 바인딩을 덮어씁니다. 여러 모듈/명령어가 필요하면 하나의 애드온 안에서 <code>handle</code> 값으로 분기하세요. <code>java()</code> 실행은 BeanShell 콜백을 넘겨야 해서 네이티브 애드온에서는 지원하지 않습니다.",
      apiNativeBuildTitle: "컴파일 방법",
      apiNativeBuildNote: "JDK의 <code>include</code> 폴더에 있는 <code>jni.h</code>/<code>jni_md.h</code> 헤더가 필요합니다. 결과물(.dll/.so/.dylib)을 <code>meteor-client/addons/</code>에 넣으면 됩니다.",
      apiNativeBuildCode: '# Windows (MinGW gcc)\ngcc -shared -I"%JAVA_HOME%\\include" -I"%JAVA_HOME%\\include\\win32" -o example.dll example.c\n\n# Linux\ngcc -shared -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" -o example.so example.c\n\n# macOS\nclang -shared -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin" -o example.dylib example.c',
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

  // These contain HTML (links/tags/entities), so render with innerHTML instead of textContent.
  ["downloadNote", "addonsNote", "footerBasedOn", "apiIntro", "apiCallbackCode", "apiExternalNote", "apiNativeIntro", "apiNativeReturnNote", "apiNativeBuildNote", "apiNativeRegisterModule", "apiNativeRegisterCommand", "apiNativeInvoke", "apiNativeCallbackNote", "apiNativeCallbackCode", "apiNativeCallbackNote2"].forEach((key) => {
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
