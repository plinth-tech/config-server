# config-server
Hierarchical json configuration management for multitenant environments

---
### Data Modeling

<table>
 <tr>
  <td>

|Config|
|:-:|
|ID|
|Base_ID|
|Version|
|Data|
|Tenant|
|Date_Create|
  </td>
  <td>

|Base|
|:-:|
|ID|
|Version|
|Data|
|Date_Create|
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