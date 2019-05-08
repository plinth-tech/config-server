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
|base_id|LONG|
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
