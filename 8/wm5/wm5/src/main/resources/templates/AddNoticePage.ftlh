<#import "/spring.ftl" as spring>
<#import "./common.ftlh" as c>

<#macro error field>
    <@spring.bind field/>
    <#list spring.status.errorMessages as error>
        <div class="error">${error}</div>
        <#break/>
    </#list>
</#macro>

<@c.page>
<div class="form-box">
    <div class="header">Add Notice</div>
    <div class="body">
        <form method="post" action="/notice">
            <div class="field">
                <div class="name">
                    <label for="text">Text</label>
                </div>
                <div class="value">
                    <textarea id="text" rows="10" name="text">${noticeForm.text!?html}</textarea>
                </div>
                <div class="name">
                    <label for="tagsStr">Tags</label>
                </div>
                <div class="value">
                    <input id="tagsStr" name="tagsStr" value="${noticeForm.tagsStr!?html}">
                </div>
                <@error 'noticeForm.text'/>
                <@error 'noticeForm.tagsStr'/>
            </div>

            <div class="button-field">
                <input type="submit" value="Add">
            </div>
            <div></div>
        </form>
    </div>
</div>
</@c.page>
