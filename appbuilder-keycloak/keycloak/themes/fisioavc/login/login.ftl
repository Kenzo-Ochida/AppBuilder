<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Entrar FisioAvc</title>
  <script src="https://cdn.tailwindcss.com"></script>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
  <link rel="stylesheet" href="style.css" />
</head>
<body class="font-sans bg-gray-100">
  <div class="flex min-h-screen">
    <!-- Left side - Login Form -->
    <div class="w-full md:w-1/2 flex items-center justify-center p-8">
      <div class="login-box w-full max-w-md bg-[#f8fafc] rounded-xl shadow-xl overflow-hidden flex flex-col">
        <!-- Header -->
          <div class="bg-[#ffffff] p-6 flex items-center">
            <img
              src="/keycloack-theme/fisioavc/login/resources/img/FisioAvc-Logo.png"
              alt="Logo FisioAVC"
              class="h-12 w-auto rounded-md"
            />
          </div>

        <!-- Form -->
        <form id="kc-form-login" method="post" action="${url.loginAction}" class="p-8 flex-1">
          <div class="mb-6">
            <label for="username" class="block text-gray-700 text-sm font-bold mb-2">Usuário</label>
            <div class="relative">
              <div class="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none">
                <i class="fas fa-user text-gray-400"></i>
              </div>
              <input
                tabindex="1"
                id="username"
                name="username"
                value="${login.username!''}"
                type="text"
                placeholder="Digite seu usuário"
                class="input-field w-full pl-10 pr-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
          </div>

          <div class="mb-6">
            <label for="password" class="block text-gray-700 text-sm font-bold mb-2">Senha</label>
            <div class="relative">
              <div class="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none">
                <i class="fas fa-lock text-gray-400"></i>
              </div>
              <input
                tabindex="2"
                id="password"
                name="password"
                type="password"
                placeholder="********"
                class="input-field w-full pl-10 pr-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
          </div>

          <input
            tabindex="4"
            name="login"
            id="kc-login"
            type="submit"
            value="Entrar"
            class="w-full bg-[#4169E1] hover:bg-[#3554c0] text-white font-bold py-3 px-4 rounded-lg transition duration-300 ease-in-out"
          />
        </form>

        <!-- Footer -->
        <div class="bg-[#f1f1f1] p-4 flex justify-center">
          <img src="https://mackcloud.mackenzie.br/apps/cdn/logomack.png" alt="Mackenzie Logo" class="h-10" />
        </div>
      </div>
    </div>

    <!-- Right side - Imagem translúcida com conteúdo por cima -->
    <div class="hidden md:flex md:w-1/2 relative items-center justify-center p-12 overflow-hidden"
         style="background-image: url('${url.resourcesPath}/img/foto-fundo.png'); background-size: cover; background-position: center;">
      
      <!-- Camada escura transparente por cima da imagem -->
      <div class="absolute inset-0 bg-white opacity-70 z-0"></div>

      <!-- Conteúdo visível normalmente -->
      <div class="relative z-10 text-center">
        <h2 class="text-4xl font-bold text-black mb-6">Bem-vindo ao FisioAvc</h2>
        <p class="text-xl text-black-700 mb-8">
          Um site criado especialmente para apoiar sua reabilitação pós-AVC, com cuidado, tecnologia e orientação profissional a cada passo.
        </p>

        <!-- Partner Grid -->
        <div class="grid grid-cols-2 gap-6 max-w-lg mx-auto">
          <img src="${url.resourcesPath}/img/ccbs-logo.png" alt="CCBS" class="h-20 mx-auto" />
          <img src="${url.resourcesPath}/img/fci-logo.png" alt="FCI" class="h-20 mx-auto" />
          <img src="${url.resourcesPath}/img/mackleaps-logo.png" alt="MackLeaps" class="h-20 mx-auto" />
          <img src="${url.resourcesPath}/img/logo-mackcloud.png" />
          

        </div>
      </div>
    </div>
  </div>

  <!-- Validação -->
  <script>
    document.getElementById('kc-form-login').addEventListener('submit', function (e) {
      const username = document.getElementById('username').value.trim();
      const password = document.getElementById('password').value.trim();
      const btn = document.getElementById('kc-login');

      if (!username || !password) {
        e.preventDefault();
        alert('Por favor, preencha todos os campos!');
        return;
      }

      btn.disabled = true;
      btn.value = 'Entrando...';
    });
  </script>
</body>
</html>
