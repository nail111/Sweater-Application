<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>
<@c.page>
Registration page
${message?ifExists}
<@l.login "/registration" />
<a href="/login">Back to the login page</a>
</@c.page>