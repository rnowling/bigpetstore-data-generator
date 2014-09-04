<?php

/**
 * @file
 * General administration functions.
 */

/**
 * Form builder function for module settings.
 */
function namedb_admin_settings_form($form, &$form_state) {
  $settings = namedb_settings();
  $form['namedb_settings'] = array('#tree' => TRUE);
  foreach (array('user_data' => t('User inputed data'), 'imports' => t('Admin imports')) as $data_key => $title) {
    $form['namedb_settings'][$data_key] = array(
      '#type' => 'fieldset',
      '#title' => $title,
    );
    $form['namedb_settings'][$data_key]['minimum_length'] = array(
      '#type' => 'select',
      '#title' => t('Minimum length'),
      '#required' => TRUE,
      '#options' => drupal_map_assoc(range(1, 10, 1)),
      '#default_value' => $settings[$data_key]['minimum_length'],
    );
    $form['namedb_settings'][$data_key]['hyphenated'] = array(
      '#type' => 'checkbox',
      '#title' => t('Allow hyphenated names to pass the test, (e.g. Lee-Smith)'),
      '#default_value' => $settings[$data_key]['hyphenated'],
    );
    $pass_defaults = array();
    foreach ($settings[$data_key]['passed'] as $key => $value) {
      if ($value) {
        $pass_defaults[$key] = $key;
      }
    }
    $form['namedb_settings'][$data_key]['passed'] = array(
      '#type' => 'checkboxes',
      '#title' => t('Insertion options for <strong>passed</strong> results'),
      '#options' => array(
        'insert' => t('Insert the name.'),
        'status' => t('Publish inserted the words. <em>Excludes updates.</em>'),
        'banned' => t('Ban inserted words. <em>Excludes updates.</em>'),
      ),
      '#default_value' => $pass_defaults,
      '#description' => t('All given and family names go through a simple cleaning function that will fail if the pattern does not meet one of the predefined formats.'),
    );
    $failed_defaults = array();
    foreach ($settings[$data_key]['failed'] as $key => $value) {
      if ($value) {
        $failed_defaults[$key] = $key;
      }
    }
    $form['namedb_settings'][$data_key]['failed'] = array(
      '#type' => 'checkboxes',
      '#title' => t('Insertion options for <strong>failed</strong> results'),
      '#options' => array(
        'insert' => t('Insert the name.'),
        'status' => t('Publish inserted the words. <em>Excludes updates.</em>'),
        'banned' => t('Ban inserted words. <em>Excludes updates.</em>'),
      ),
      '#default_value' => $failed_defaults,
      '#description' => t('All given and family names go through a simple cleaning function that will fail if the pattern does not meet one of the predefined formats.'),
    );
  }

  return system_settings_form($form);
}

/**
 * Lists the known names.
 */
function namedb_list_names($keys = NULL, $components = NULL) {
  $header = array();
  $header[] = array('data' => t('Name'), 'field' => 'name', 'sort' => 'asc');
  $header[] = array('data' => t('Abbr.'), 'field' => 'abbr');

  $header[] = array('data' => t('Title'), 'field' => 'title');
  $header[] = array('data' => t('Given'), 'field' => 'given');
  $header[] = array('data' => t('Family'), 'field' => 'family');
  $header[] = array('data' => t('Gen.'), 'field' => 'generational');
  $header[] = array('data' => t('Cred.'), 'field' => 'credentials');

  $header[] = array('data' => t('Status'), 'field' => 'status');
  $header[] = array('data' => t('Banned'), 'field' => 'banned');
  $header[] = array('data' => t('Operations'));

  // Add the filter form above the overview table.
  $build['namedb_filter_form'] = drupal_get_form('namedb_filter_form', $keys, $components);

  $query = db_select('name_database')->extend('PagerDefault')->extend('TableSort');
  if ($keys) {
    // Replace wildcards with PDO wildcards.
    $query->condition('name', '%' . preg_replace('!\*+!', '%', $keys) . '%', 'LIKE');
  }
  if ($components) {
    $components = explode('][', $components);
    foreach (array('title', 'given', 'family', 'generational', 'credentials', 'banned', 'status') as $component) {
      if (in_array($component, $components)) {
        $query->condition($component, 1);
      }
    }
  }
  $result = $query
    ->fields('name_database')
    ->orderByHeader($header)
    ->limit(50)
    ->execute();

  $rows = array();
  $destination = drupal_get_destination();
  foreach ($result as $data) {
    $row = array();

    $row['data']['name'] = array('data' => $data->name);
    $row['data']['abbr'] = array('data' => $data->abbr);

    $row['data']['title'] = array('data' => $data->title);
    $row['data']['given'] = array('data' => $data->given);
    $row['data']['family'] = array('data' => $data->family);
    $row['data']['generational'] = array('data' => $data->generational);
    $row['data']['credentials'] = array('data' => $data->credentials);

    $row['data']['status'] = array('data' => $data->status);
    $row['data']['banned'] = array('data' => $data->banned);

    $operations = array();
    $operations['edit'] = array(
      'title' => t('edit'),
      'href' => "admin/config/regional/name/namedb/{$data->ndbid}",
      'query' => $destination,
    );
    $operations['delete'] = array(
      'title' => t('delete'),
      'href' => "admin/config/regional/name/namedb/{$data->ndbid}/delete",
      'query' => $destination,
    );
    $row['data']['operations'] = array(
      'data' => array(
        '#theme' => 'links',
        '#links' => $operations,
        '#attributes' => array('class' => array('links', 'inline', 'nowrap')),
      ),
    );

    // Clearly show up banned names.
    if ($data->banned) {
      $row['class'] = array('warning');
    }

    $rows[] = $row;
  }

  $build['name_table'] = array(
    '#theme' => 'table',
    '#header' => $header,
    '#rows' => $rows,
    '#empty' => t('No names are available. <a href="@link">Add a name</a>.', array('@link' => url('admin/config/regional/name/namedb/add'))),
  );
  $build['name_pager'] = array('#theme' => 'pager');

  return $build;
}

/**
 * Return a form to filter URL aliases.
 *
 * @ingroup forms
 * @see namedb_filter_form_submit()
 */
function namedb_filter_form($form, &$form_state, $keys = '', $options = '') {
  $form['#attributes'] = array('class' => array('search-form'));
  $form['basic'] = array('#type' => 'fieldset',
    '#title' => t('Filter names'),
    //'#attributes' => array('class' => array('container-inline clearfix')),
    'buttons' => array(
      '#weight' => 1,
    ),
  );
  $form['basic']['filter'] = array(
    '#type' => 'textfield',
    '#title' => 'Key words',
    '#default_value' => $keys,
    '#maxlength' => 128,
    '#size' => 25,
    '#prefix' => '<div class="container-inline clearfix">',
    '#suffix' => '</div>',
    '#autocomplete_path' => 'name/autocomplete/-/name-all',
  );
  $form['basic']['components'] = array(
    '#type' => 'checkboxes',
    '#title' => 'Components',
    '#default_value' => explode('][', $options),
    '#attributes' => array('class' => array('container-inline')),
    '#options' => array('title' => t('Title'), 'family' => t('Family'),
        'given' => t('Given'), 'generational' => t('Generational'),
        'credentials' => t('Credentials'), 'banned' => t('Banned'),
        'status' => t('Status')),
    '#prefix' => '<div class="container-inline clearfix">',
    '#suffix' => '</div>',
  );
  $form['basic']['buttons']['submit'] = array(
    '#type' => 'submit',
    '#value' => t('Filter'),
    '#submit' => array('namedb_filter_form_submit_filter'),
    );
  if ($keys || $options) {
    $form['basic']['buttons']['reset'] = array(
      '#type' => 'submit',
      '#value' => t('Reset'),
      '#submit' => array('namedb_filter_form_submit_reset'),
    );
  }
  return $form;
}

/**
 * Process filter form submission when the Filter button is pressed.
 */
function namedb_filter_form_submit_filter($form, &$form_state) {
  $form_state['redirect'] = 'admin/config/regional/name/namedb/list/'
    . trim($form_state['values']['filter']) . '/'
    . implode('][', array_filter($form_state['values']['components']));
}

/**
 * Process filter form submission when the Reset button is pressed.
 */
function namedb_filter_form_submit_reset($form, &$form_state) {
  $form_state['redirect'] = 'admin/config/regional/name/namedb/list';
}

/**
 * Form callback to edit or add a new name.
 */
function namedb_name_form($form, $form_state, $edit = array()) {
  $edit += array(
    'ndbid' => NULL,
    'name' => '',
    'abbr' => '',
    'title' => 0,
    'given' => 0,
    'family' => 0,
    'generational' => 0,
    'credentials' => 0,
    'status' => 1,
    'banned' => 0,
  );

  $form = array();
  $form['ndbid'] = array(
    '#type' => 'value',
    '#value' => $edit['ndbid'],
  );
  $form['name'] = array(
    '#type' => 'textfield',
    '#title' => t('Name'),
    '#default_value' => $edit['name'],
    '#maxlength' => 255,
    '#required' => TRUE,
  );

  $form['abbr'] = array(
    '#title' => t('Abbr.'),
    '#description' => t('An abbrivation for the name. This is mainly only useful for titles and credentials.'),
    '#type' => 'textfield',
    '#default_value' => $edit['abbr'],
  );
  foreach (array('title', 'given', 'family', 'generational', 'credentials') as $component) {
    $form[$component] = array(
      '#type' => 'checkbox',
      '#title' => t('Used for %key.', array('%key' => $component)),
      '#default_value' => $edit[$component],
    );
  }
  $form['status'] = array(
    '#type' => 'checkbox',
    '#title' => t('Flag that this name should be displayed to users.'),
    '#default_value' => $edit['status'],
  );
  $form['banned'] = array(
    '#type' => 'checkbox',
    '#title' => t('Flag that this name should not be allowed as a name componenet.'),
    '#default_value' => $edit['banned'],
  );

  $form['submit'] = array(
    '#type' => 'submit',
    '#value' => t('Save'),
  );
  if (!empty($edit['ndbid'])) {
    $form['delete'] = array(
      '#type' => 'submit',
      '#value' => t('Delete'),
    );
  }
  return $form;
}

/**
 * Custom validation for namedb_name_form().
 */
function namedb_name_form_validate($form, &$form_state) {
  $values = $form_state['values'];
  // Ensure that the name is unique
  if (empty($values['ndbid'])) {
    $count = db_query_range("SELECT 1 FROM {name_database} WHERE name = :name",
        0, 1, array(':name' => $values['name']))->fetchField();
  }
  else {
    $count = db_query_range("SELECT 1 FROM {name_database} WHERE name = :name AND ndbid <> :ndbid",
        0, 1, array(':name' => $values['name'], ':ndbid' => $values['ndbid']))->fetchField();
  }
  if ($count) {
    form_set_error('name', t('The name you have chosen is already in use.'));
  }
}

/**
 * Submit handler for namedb_name_form().
 */
function namedb_name_form_submit($form, &$form_state) {
  $values = $form_state['values'];
  $values['normalized'] = strtoupper(name_translite_word($form_state['values']['name']));
  if (empty($values['ndbid'])) {
    drupal_write_record('name_database', $values);
    $message = 'Name %name has been created.';
  }
  else {
    drupal_write_record('name_database', $values, 'ndbid');
    $message = 'Name %name has been updated.';
  }
  drupal_set_message(t($message, array('%name' => $values['name'])));

  $form_state['redirect'] = 'admin/config/regional/name/namedb';
}

/**
 * Page to edit a name.
 */
function namedb_name_edit($ndbid) {
  if ((isset($_POST['op']) && $_POST['op'] == t('Delete')) || isset($_POST['confirm'])) {
    return drupal_get_form('namedb_name_delete_form', $ndbid);
  }

  if ($name = db_query("SELECT ndb.* FROM {name_database} ndb WHERE ndbid = :ndbid", array(':ndbid' => $ndbid))->fetchAssoc()) {
    return drupal_get_form('namedb_name_form', $name);
  }
  drupal_set_message(t('The name could not be found.'), 'error');
  drupal_goto('admin/config/regional/name/namedb');
}

/**
 * Name deletion form.
 */
function namedb_name_delete_form($form, $form_state, $ndbid) {
  $name = db_query("SELECT ndb.* FROM {name_database} ndb WHERE ndbid = :ndbid", array(':ndbid' => $ndbid))->fetchAssoc();

  if (!$name) {
    drupal_set_message(t('The name could not be found.'), 'error');
    drupal_goto('admin/config/regional/name/namedb');
  }
  $form = array();
  $form['ndbid'] = array(
    '#type' => 'value',
    '#value' => $name['ndbid'],
  );
  $form['#name'] = $name;
  return confirm_form(
    $form,
    t('Are you sure you want to delete the name %name?', array('%name' => $name['name'])
    ),
    'admin/config/regional/name/namedb',
    t('This action cannot be undone.'),
    t('Delete'),   t('Cancel')
  );
}

/**
 * Submit handler for namedb_name_delete_form().
 */
function namedb_name_delete_form_submit($form, &$form_state) {
  db_delete('name_database')
      ->condition('ndbid', $form_state['values']['ndbid'])
      ->execute();
  drupal_set_message(t('The name %name was deleted.',
      array('%name' => $form['#name']['name'])));
  $form_state['redirect'] = 'admin/config/regional/name/namedb';
}

/**
 * The form callback for importing predefined name .dat files.
 */
function namedb_import_form($form, &$form_state) {
  $settings = namedb_settings();
  $enabled = ($settings['imports']['passed']['insert'] || $settings['imports']['failed']['insert']);
  if (!$enabled) {
    drupal_set_message(t('There are no insertion rules enabled. At least one insert option must be selected for the administration import to work.'), 'warning');
  }
  $form['imports'] = array(
    '#type' => 'checkboxes',
    '#title' => t('Select data files to import.'),
    '#options' => array(
      'family' => t('Family names: Approx 50,000 names from USA cenus data.'),
      'given' => t('Given names: Approx 85,000 names from USA cenus data.'),
      'title' => t('Titles: 40 titles from Mr to Lieutenant Commander.'),
    ),
    '#required' => TRUE,
  );

  $form['submit'] = array(
    '#type' => 'submit',
    '#value' => t('Import'),
    '#disabled' => !$enabled
  );

  return $form;
}

/**
 * The submit handler for namedb_import_form().
 */
function namedb_import_form_submit($form, &$form_state) {
  $imports = array_filter($form_state['values']['imports']);
  $contents = file_get_contents(drupal_get_path('module', 'namedb') . '/data/data.dat');
  $lines = explode("\n", $contents);
  $counter = 0;
  foreach ($lines as $line => $name) {
    if (empty($line) || strpos($line, ';')) {
      continue;
    }
    if ($line % 100 === 0) {
      $counter++;
    }
    if (!isset($batched_names[$counter])) {
      $batched_names[$counter] = array();
    }
    $batched_names[$counter][] = $name;
  }
  namedb_batch_name_import($batched_names, $imports);
}

/**
 * The batch handler for namedb_import_form_submit().
 *
 * This is used if there are more than 50 individual names imported at any
 * one time.
 */
function namedb_batch_name_import($batched_names, $imports) {

  $total = count($batched_names);
  $operations = array();
  foreach ($batched_names as $index => $batched_name_group) {
    $operations[] = array(
      'namedb_batch_name_import_process',
      array(
        $batched_name_group, $imports, $index, $total,
      ),
    );
  }

  $batch = array(
    'operations' => $operations,
    'finished' => 'namedb_batch_name_import_finished',
    'title' => t('Processing %key name import', array('%key' => implode(', ', $imports))),
    'init_message' => t('Name Import is starting.'),
    'progress_message' => t('Processed @current out of @total %key name blocks.', array('%key' => implode(', ', $imports))),
    'error_message' => t('Name import has encountered an error.'),
    'file' => drupal_get_path('module', 'namedb') . '/namedb.admin.inc',
  );
  batch_set($batch);
}

/**
 * Batch 'operation' callback for namedb_import_form_submit().
 */
function namedb_batch_name_import_process($batched_name_group, $imports, $index, $total, &$context) {
  $file = drupal_get_path('module', 'name') . '/' . 'i18n-ascii.txt';
  $letters = 'a-z' . implode('', array_keys(parse_ini_file($file)));

  $context['results'] = array('passed' => array(), 'failed' => array());
  $context['sandbox']['progress'] = $index;
  $context['sandbox']['max'] = $total;

  $imports = drupal_map_assoc($imports);

  foreach ($batched_name_group as $line) {
    list($name, $abbr, $title, $family, $given, $weight) = $item = explode(',', $line, 6);
    $item = array_combine(array('name', 'abbr', 'title', 'family', 'given', 'weight'), $item);
    if (is_null($weight)) {
      $weight = 1024;
    }
    $row_flags = array_filter(array_intersect_key($item, $imports));
    if (empty($row_flags)) {
      continue;
    }
    $old_name = $name;
    if ($name = name_process_name_component($name, 'imports', $letters)) {
      if (namedb_update_database(array_keys($row_flags), $name, $abbr, 'imports', $weight)) {
        $context['results']['passed'][] = check_plain($name);
      }
      else {
        $context['results']['failed'][] = check_plain($name);
      }
    }
    else {
      $context['results']['failed'][] = check_plain($old_name) .'--'. name_process_name_component($name, 'imports');
    }
  }
  // Update our progress information.
  $message = t('Successfully imported or updated %c1 %key names, failed on %c2 names.',
    array('%key' => $key, '%c1' => count($context['results']['passed']), '%c2' => count($context['results']['failed'])));
  if (!empty($context['results']['failed'])) {
    $message .= t('Failed imports:') . implode(', ', $context['results']['failed']) ;
  }
  $context['message'] = $message;
  $context['finished'] = 1;
}

/**
 * Batch 'finished' callback for namedb_import_form_submit().
 */
function namedb_batch_name_import_finished($success, $results, $operations) {
  if ($success) {
    // Here we do something meaningful with the results.
    $message = t('Import complete');
  }
  else {
    // An error occurred.
    // $operations contains the operations that remained unprocessed.
    $error_operation = reset($operations);
    $message = t('An error occurred while processing %error_operation with arguments: @arguments', array('%error_operation' => $error_operation[0], '@arguments' => print_r($error_operation[1], TRUE)));
  }
  drupal_set_message($message);
}

