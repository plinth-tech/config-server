# config-server
Hierarchical json configuration management for multitenant environments

---
### Data Modeling

<table>
 <tr>
  <td>

|config||
|:-:|:-:|
|id|INT|
|base_id|INT|
|version|VARCHAR(255)|
|data|JSON|
|tenant|VARCHAR(255)|
|created_at|TIMESTAMP|
  </td>
  <td>

|base||
|:-:|:-:|
|id|INT|
|version|VARCHAR(255)|
|data|JSON|
|created_at|TIMESTAMP|
  </td>
 </tr>
 <tr>
  <td>
  

* ID: Identify the new entrance;

* Base_ID: Refer to each *Base JSON* are we versioning;

* Version: Identify the new *JSON* update;

* Data: Is the concrete *JSON* data that differs from the *Base JSON*;

* Tenant: The tenant being served;

* Date_Create: Keep the information when this version was created.
  </td>
<td>

* ID: Identify the new entrance;

* Version: Identify the new *JSON* update of the *Base*;

* Data: Is the concrete *JSON* data;

* Date_Create: Keep the information when this version is created.
</td>
 </tr>
</table>
