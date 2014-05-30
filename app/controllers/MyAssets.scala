package controllers

import play.api.mvc.Controller

/**
 * Created by mati on 30/05/2014.
 */
object MyAssets extends Controller {

  def versioned(path: String) = routes.Assets.versioned(path).url

}
