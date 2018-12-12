<#import "common.ftl" as c>

<@c.page>
<div class="form-box">
    <div class="header">Confirm</div>
    <div class="body">
        <form method="post" action="/confirm">
            <input type="hidden" name="action" value="confirm">
            <div class="field">
                <div class="name">
                    <label for="secret">Secret</label>
                </div>
                <div class="value">
                    <input id="secret" name="secret" value="${secret!}"/>
                </div>
            </div>
            <div class="button-field">
                <input type="submit" value="Enter">
            </div>
            <#if error??>
            <div class="error">
                ${error}
            </div>
            </#if>
        </form>
    </div>
</div>
</@c.page>
