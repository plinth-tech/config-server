# config-server
Hierarchical json configuration management for multitenant environments

---

### Data Modeling

<table>
 <tr>
  <td>

|config||
|:-:|:-:|
|id|LONG|
|version|LONG|
|data|JSON|
|tenant|VARCHAR(255)|
|created_at|TIMESTAMP|
  </td>
  <td>

|base||
|:-:|:-:|
|id|LONG|
|version|LONG|
|data|JSON|
|created_at|TIMESTAMP|
  </td>
 </tr>
 <tr>
  <td>

* id: Identify the new entrance;

* version: Identify the new *JSON* update;

* data: Is the concrete *JSON* data that differs from the *Base JSON*;

* tenant: The tenant being served;

* created_at: Keep the information when this version was created.
  </td>
<td>

* id: Identify the new entrance;

* version: Identify the new *JSON* update of the *Base*;

* data: Is the concrete *JSON* data;

* created_at: Keep the information when this version is created.
</td>
 </tr>
</table>

---

### URI endpoints

|paths|||
|:-|:-|:-|
|config/|GET, POST|Get the last version (all scopes from this version), or create a new version|
|config/?version={version}|GET|Get a specific version (all scopes from this version)|
|config/scopes/{scope}|GET|Get a specific scope of last version|
|bases/|GET|Get base json file|

##### Example of scenes:

* update scopes
    * make a post with some update scopes (POST config/) in json format;	
    * create a new version and save the scopes updated/created;	
    * return the scopes posted/saved.

* check the last configuration version of base inserted
    * make a GET *config/* ;
    * if exist, receive the last version posted.

