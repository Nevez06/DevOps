# DevOps
## Alterações adicionadas

- Visual modernizado nas telas com cards, gradientes, botões e campos mais limpos.
- Login com link **Esqueci minha senha**.
- Fluxo funcional de recuperação de senha:
  - solicitação por email em `/recuperar-senha`;
  - token seguro com validade de 30 minutos;
  - redefinição em `/redefinir-senha?token=...`;
  - senha salva novamente com BCrypt.

## Configuração do envio de email

Configure as variáveis de ambiente antes de rodar o projeto:

```bash
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=seuemail@gmail.com
MAIL_PASSWORD=sua_senha_de_app
MAIL_FROM=seuemail@gmail.com
APP_BASE_URL=http://localhost:8080
```

Para Gmail, use uma **senha de app** da conta Google. Sem SMTP configurado, o sistema mostra um link de teste em modo desenvolvimento na tela de recuperação.
