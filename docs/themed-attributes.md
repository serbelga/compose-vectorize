# Themed Attributes

It also supports theme attributes. If you are using theme color attributes, like `?attr/colorPrimary` 
or `?attr/colorSecondary`, they can be converted to Compose `MaterialTheme` tokens, which means that 
you can update illustration colors based on the token values. It is also compatible with Material3 dynamic colors.

```xml
    <path
        android:pathData="..."
        android:fillColor="?attr/colorPrimary"/>
```

<div>
    <img width="220" alt="Screenshot_20240310_145842" src="https://github.com/serbelga/compose-vectorize/assets/26246782/73ebdb93-fbaf-454f-aaa2-bf17d9d2dff1">
    <img width="220" alt="Screenshot_20240310_145842-2" src="https://github.com/serbelga/compose-vectorize/assets/26246782/b282aff3-599b-4039-9455-247eed7befc2">
    <img width="220" alt="Screenshot_20240310_145842-1" src="https://github.com/serbelga/compose-vectorize/assets/26246782/e2a10a2e-1eb4-40af-be1e-aeaac1954f23">
</div>

!!! info

    It requires use Compose Material 3 dependency.
